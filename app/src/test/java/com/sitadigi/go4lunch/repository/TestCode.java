package com.sitadigi.go4lunch.repository;


public class TestCode {

  /*  private static final FirestoreOptions FIRESTORE_OPTIONS = FirestoreOptions.newBuilder()
            // Setting credentials is not required (they get overridden by Admin SDK), but without
            // this Firestore logs an ugly warning during tests.
            .setCredentials(new MockGoogleCredentials("test-token"))
            .build();

    @After
    public void tearDown() {
        //TestOnlyImplFirebaseTrampolines.clearInstancesForTest();
    }

    @Test
    public void testExplicitProjectId() throws IOException {
      /*  InputStream in =
                getClass().getResourceAsStream("/firebase-adminsdk.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredential(FirebaseCredentials.fromCertificate(in))
                .setDatabaseUrl("<db-url-here>")
                .build();

        FirebaseApp app= FirebaseApp.initializeApp(options);*/
       /* FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.getApplicationDefault()
                    //    /*fromStream(ServiceAccount.EDITOR.asStream())*///)
               /* .setProjectId("explicit-project-id")
                .setFirestoreOptions(FIRESTORE_OPTIONS)
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(options);*/
    /*    Firestore firestore = FirestoreClient.getFirestore(app);
        assertEquals("explicit-project-id", firestore.getOptions().getProjectId());

        firestore = FirestoreClient.getFirestore();
        assertEquals("explicit-project-id", firestore.getOptions().getProjectId());
    }

     */

 /*   @Test
    public void testServiceAccountProjectId() throws IOException {
        FirebaseApp app = FirebaseApp.initializeApp(FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault()
                        /*fromStream(ServiceAccount.EDITOR.asStream())*/
/*)
                .setFirestoreOptions(FIRESTORE_OPTIONS)
                .build());
        Firestore firestore = FirestoreClient.getFirestore(app);
        assertEquals("mock-project-id", firestore.getOptions().getProjectId());

        firestore = FirestoreClient.getFirestore();
        assertEquals("mock-project-id", firestore.getOptions().getProjectId());
    }
    */

  /*  @Test
    public void testFirestoreOptions() throws IOException {
        FirebaseApp app = FirebaseApp.initializeApp(FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault()
                        /*fromStream(ServiceAccount.EDITOR.asStream())*/
/*)
                .setProjectId("explicit-project-id")
                .setFirestoreOptions(FIRESTORE_OPTIONS)
                .build());
        Firestore firestore = FirestoreClient.getFirestore(app);
        assertEquals("explicit-project-id", firestore.getOptions().getProjectId());

        firestore = FirestoreClient.getFirestore();
        assertEquals("explicit-project-id", firestore.getOptions().getProjectId());
    }
*/
 /*   @Test
    public void testFirestoreOptionsOverride() throws IOException {
        FirebaseApp app = FirebaseApp.initializeApp(FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault()
                        /*fromStream(ServiceAccount.EDITOR.asStream())*/
/*)
                .setProjectId("explicit-project-id")
                .setFirestoreOptions(FirestoreOptions.newBuilder()
                        .setProjectId("other-project-id")
                        .setCredentials(GoogleCredentials.getApplicationDefault()
                                /*fromStream(ServiceAccount.EDITOR.asStream())*/
/*)
                        .build())
                .build());
        Firestore firestore = FirestoreClient.getFirestore(app);
        assertEquals("explicit-project-id", firestore.getOptions().getProjectId());
        assertSame(ImplFirebaseTrampolines.getCredentials(app),
                firestore.getOptions().getCredentialsProvider().getCredentials());

        firestore = FirestoreClient.getFirestore();
        assertEquals("explicit-project-id", firestore.getOptions().getProjectId());
        assertSame(ImplFirebaseTrampolines.getCredentials(app),
                firestore.getOptions().getCredentialsProvider().getCredentials());
    }
*/
 /*   @Test
    public void testAppDelete() throws IOException {
        FirebaseApp app = FirebaseApp.initializeApp(FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault()
                 //       /*fromStream(ServiceAccount.EDITOR.asStream())*/
/*   )
                .setProjectId("mock-project-id")
                .setFirestoreOptions(FIRESTORE_OPTIONS)
                .build());

        Firestore firestore = FirestoreClient.getFirestore(app);
        assertNotNull(firestore);
        DocumentReference document = firestore.collection("collection").document("doc");
        app.delete();
        try {
            FirestoreClient.getFirestore(app);
            fail("No error thrown for deleted app");
        } catch (IllegalStateException expected) {
            // ignore
        }

        try {
            document.get();
            fail("No error thrown for deleted app");
        } catch (IllegalStateException expected) {
            // ignore
        }

        try {
            FirestoreClient.getFirestore();
            fail("No error thrown for deleted app");
        } catch (IllegalStateException expected) {
            // ignore
        }
    }
    */
}
