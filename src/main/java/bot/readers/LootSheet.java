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

public class LootSheet {
    protected static Dotenv dotenv = Dotenv.load();
    protected static final String APPLICATION_NAME = dotenv.get("APPLICATION_NAME_LOOT") ;
    protected static final String SPREADSHEET_ID = dotenv.get("SPREADSHEET_ID_LOOT");

    protected static Credential authorize() throws IOException, GeneralSecurityException {
        InputStream in = LootSheet.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(),new InputStreamReader(in));

        final java.util.logging.Logger buggyLogger = java.util.logging.Logger.getLogger(FileDataStoreFactory.class.getName());
        buggyLogger.setLevel(java.util.logging.Level.SEVERE);


        List<String> scopes = Collections.singletonList(SheetsScopes.SPREADSHEETS);
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),JacksonFactory.getDefaultInstance(),
                clientSecrets,scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(
                flow,new LocalServerReceiver())
                .authorize(("user"));
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
        String range = "Loot!A2:B75";
        File file = new File("numbers.txt");
        PrintStream numbers = new PrintStream(file);



        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID,range)
                .execute();
        List<List<Object>> values = response.getValues();

        if(values == null || values.isEmpty()) {
            System.out.println("No Values");
        }
        else{
            for(List row: values){
                if(row.get(0).toString().startsWith("Average"))
                    break;
                if(row.size()<2) {
                    numbers.println(row.get(0).toString().replaceAll(" ","")+" "+0);
                    continue;
                }
                String loot = row.get(1).toString();
                int i =Integer.parseInt(loot.replaceAll(",",""));
                String name = row.get(0).toString();
                String k = name.replaceAll(" ","");
                numbers.printf("%s %d%n",k,i);
            }

        }

    }



}
