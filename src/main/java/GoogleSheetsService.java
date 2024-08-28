import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleSheetsService {

    private static final String APPLICATION_NAME = "BookLender";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/Users/fabioperettiguimaraes/Documents/JSON/client_secret_1045581328574-4rsm0ijgg5f64n6kjdtersqm54ve7uhr.apps.googleusercontent.com.json"; // Substitua com o caminho correto

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Carregar o arquivo de credenciais
        FileInputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Construir o fluxo de autorização e o receptor local para gerenciar o código de autenticação
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public Sheets getSheetsService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    public static void main(String[] args) {
        try {
            GoogleSheetsService sheetsService = new GoogleSheetsService();
            Sheets service = sheetsService.getSheetsService();

            // Substitua pelo ID da sua planilha
            String spreadsheetId = "1RxPefnCBi8CSqtkmeEHI66JaK1QSFuRbF9Zmjkk2HHA";
            // Intervalo de células que você quer ler
            String range = "Sheet1!A1:D5";

            // Lendo os valores
            ValueRange response = service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();

            if (values == null || values.isEmpty()) {
                System.out.println("Nenhum dado encontrado.");
            } else {
                System.out.println("Nome, Autor, Ano:");
                for (List row : values) {
                    // Imprimindo os valores lidos
                    System.out.printf("%s, %s, %s\n", row.get(0), row.get(1), row.get(2));
                }
            }
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

}