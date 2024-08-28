import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            GoogleSheetsService sheetsService = new GoogleSheetsService();
            Sheets service = sheetsService.getSheetsService();


            // Criando um array de objetos Livro
            List<Livro> arrayLivros = new ArrayList<>();
            arrayLivros.add(new Livro("Harry Potter e a Pedra Filosofal", "J. K. Rowling", 1997));
            arrayLivros.add(new Livro("Harry Potter e a Câmara Secreta", "J. K. Rowling", 1998));
            arrayLivros.add(new Livro("Harry Potter e o Prizioneiro de Azkaban", "J. K. Rowling", 1999));
            arrayLivros.add(new Livro("Harry Potter e o Cálice de Fogo", "J. K. Rowling", 2000));
            arrayLivros.add(new Livro("Harry Potter e a Ordem da Fênix", "J. K. Rowling", 2003));
            arrayLivros.add(new Livro("Harry Potter e o Enigma do Príncipe", "J. K. Rowling", 2005));
            arrayLivros.add(new Livro("Harry Potter e as Reliquias da Morte", "J. K. Rowling", 2007));
            arrayLivros.add(new Livro("Orgulho e Preconceito", "Jane Austin", 1813));

            String spreadsheetId = "1RxPefnCBi8CSqtkmeEHI66JaK1QSFuRbF9Zmjkk2HHA";
            String range = "Sheet1"; // Adicionando na próxima linha disponível


            //verificando duplicatas na planilha
            ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
            List<List<Object>> livroExistente = response.getValues();

            for (Livro livro : arrayLivros) {
                boolean livroExiste = false;

                //verifica se o livro já está na planilha
                if (livroExistente != null) {
                    for (List<Object> row : livroExistente) {
                        if (!row.isEmpty() && row.get(0).toString().equals(livro.getNome())) {
                            livroExiste = true;
                            break;
                        }
                    }
                }

                //adiciona livro novo
                if (!livroExiste) {
                    List<List<Object>> valores = Arrays.asList(
                            Arrays.asList(livro.getNome(), livro.getAutor(), livro.getAno(), livro.getStatus())
                    );

                    ValueRange body = new ValueRange().setValues(valores);

                    service.spreadsheets().values()
                            .append(spreadsheetId, range, body)
                            .setValueInputOption("RAW")
                            .execute();

                    System.out.println("Livro '" + livro.getNome() + "' adicionado com sucesso :)");
                } else {
                    System.out.println("Livro '" + livro.getNome() + "' já existe na planilha então não será adicionado novamente.");
                }
            }


        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}