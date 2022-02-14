package bot.readers;

import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.security.GeneralSecurityException;
import java.util.List;

public class LootSheetAvg extends LootSheet {
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        Sheets sheetsService = getSheetsService();
        String rangeAvg = "Loot!M2:M75";
        String names = "Loot!A2:A75";
        File avg = new File("averages.txt");
        PrintStream averages = new PrintStream(avg);

        final java.util.logging.Logger buggyLogger = java.util.logging.Logger.getLogger(FileDataStoreFactory.class.getName());
        buggyLogger.setLevel(java.util.logging.Level.SEVERE);

        ValueRange responseAvg = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID,rangeAvg)
                .execute();
        List<List<Object>> avgValues = responseAvg.getValues();

        ValueRange avgNames = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID,names)
                .execute();
        List<List<Object>> nameValues = avgNames.getValues();

        if(avgValues == null || avgValues.isEmpty()) {
            System.out.println();
        }
        else{
            for(int i=0;i< nameValues.size();i++){
                if(nameValues.get(i).get(0).toString().startsWith("Average"))
                    break;
                String average = avgValues.get(i).get(0).toString();
                String avgName = nameValues.get(i).get(0).toString();

                int j =Integer.parseInt(average.replaceAll(",","")
                        .replaceAll("\\[","").replaceAll("]",""));

                String k = avgName.replaceAll(" ","")
                        .replaceAll("\\[","").replaceAll("]","");

                averages.println(k + " " + j);

            }
        }
    }
}
