package com.sitadigi.go4lunch.notifications;

import static com.sitadigi.go4lunch.ui.settings.SettingsFragment.NO;
import static com.sitadigi.go4lunch.ui.settings.SettingsFragment.SHARED_PREF_USER_INFO;
import static com.sitadigi.go4lunch.ui.settings.SettingsFragment.SHARED_PREF_USER_INFO_NOTIFICATION;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sitadigi.go4lunch.R;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sitadigi.go4lunch.MainActivity;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.UserRepository;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

import java.util.List;

public class NotificationsService extends FirebaseMessagingService {

    UserRepository mUserRepository;
    UserViewModel mUserViewModel = new UserViewModel();
    String userRestaurantName;
    String workmateName = "";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        String statusNotification = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                .getString(SHARED_PREF_USER_INFO_NOTIFICATION, null);
        if (statusNotification.equals(NO)) {
            //Do nothing
        } else {
            mUserRepository = UserRepository.getInstance();
            List<User> users = mUserRepository.getAllUserForNotificationPush();
            String userUid = mUserViewModel.getCurrentUser().getUid();
            // Get userRestaurantId on firebaseFirestore
            DocumentReference userDocumentRef = mUserViewModel.getUsersCollection().document(userUid);
            userDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            userRestaurantName = document.getString("userRestoName");
                            for (User userWithSameRestaurantName : users) {
                                if (userWithSameRestaurantName.getUserRestaurantName().equals(userRestaurantName)) {
                                    workmateName = workmateName + " , " + userWithSameRestaurantName.getUsername();
                                }
                            }
                            if (remoteMessage.getNotification() != null) {
                                // Get message sent by Firebase
                                RemoteMessage.Notification notification = remoteMessage.getNotification();
                                sendVisualNotification(notification);
                            }
                        }
                    }
                }
            });
        }
    }

    private void sendVisualNotification(RemoteMessage.Notification notification) {

        // Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        // Create a Channel (Android 8)
        String channelId = getString(R.string.default_notification_channel_id);
        String notificationMessage = " avec " + workmateName;
        // Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.fond_d_ecran)
                        .setContentTitle(" Vous mangez au: " + userRestaurantName)
                        .setContentText(notificationMessage)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Firebase Messages";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        // Show notification
        int NOTIFICATION_ID = 007;
        String NOTIFICATION_TAG = "FIREBASEOC";
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
}
