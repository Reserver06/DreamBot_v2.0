package bot.readers;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class ContactSheet {
    public static Dotenv dotenv = Dotenv.load();
    private static final String APPLICATION_NAME = dotenv.get("APPLICATION_NAME");
    private static final String SPREADSHEET_ID = dotenv.get("SPREADSHEET_ID");

    public static Credential authorize() throws IOException, GeneralSecurityException {
        InputStream in = LootSheet.class.getResourceAsStream("/credentials.json");
        assert in != null;
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(),new InputStreamReader(in));


        List<String> scopes = Collections.singletonList(SheetsScopes.SPREADSHEETS);
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),JacksonFactory.getDefaultInstance(),
                clientSecrets,scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();
        return new AuthorizationCodeInstalledApp(
                flow,new LocalServerReceiver())
                .authorize("User");
    }
    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        credential.setExpirationTimeMilliseconds(999999999999999999L);
        System.err.println(credential.getExpiresInSeconds());
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        Sheets sheetsService = getSheetsService();
        String range = "Sheet1!A2:B150";
        File file = new File("contacts.txt");
        PrintStream contacts = new PrintStream(file);

        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();
        List<List<Object>> values = response.getValues();

        if (values == null || values.isEmpty())
            System.out.println("No Values");
        else {
            for (List row : values) {
                if (row.get(0).toString().startsWith("Visitors:"))
                    continue;
                if (row.size() < 1) {
                    contacts.println(row.get(0).toString().replaceAll(" ", ""));
                    continue;
                }

                String name = row.get(0).toString();
                String k = name.replaceAll(" ", "");
                if(row.size()<2)
                    continue;
                String number = row.get(1).toString();
                contacts.printf("%s %s%n", k, number);
            }
            System.err.println(authorize().getExpiresInSeconds());
        }
    }

}
