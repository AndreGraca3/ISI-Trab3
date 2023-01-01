package jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
interface DbWorker
{
    void doWork();
}
class App{

    private enum Option {
        Unknown, // 0
        Exit, // 1
        registerPessoa, // 2
        registerCondutor, // 3
        registerProprietario, // 4
        registerVeiculo // 5
    }

    private static App __instance = null;
    private String __connectionString;

    private HashMap<Option,DbWorker> __dbMethods; // instaciação do haspmap dos metodos da app.

    private App() {
        __dbMethods = new HashMap<Option,DbWorker>();
        __dbMethods.put(Option.registerPessoa, new DbWorker() {public void doWork() {App.this.registerPessoa();}});
        __dbMethods.put(Option.registerCondutor, new DbWorker() {public void doWork() {App.this.registerCondutor();}});
        __dbMethods.put(Option.registerProprietario, new DbWorker() {public void doWork() {App.this.registerProprietario();}});
        __dbMethods.put(Option.registerVeiculo, new DbWorker() {public void doWork() {App.this.registerVeiculo();}});
    }

    public static App getInstance()
    {
        if(__instance == null)
        {
            __instance = new App();
        }
        return __instance;
    }

    private Option DisplayMenu() {

        Option option = Option.Unknown;

        try{
            System.out.println("Company management");
            System.out.println();
            System.out.println("1. Exit");
            System.out.println("2. Register Pessoa");
            System.out.println("3. Register Condutor");
            System.out.println("4. Register Proprietario");
            System.out.println("5. Register veiculo");
            System.out.print(">");
            Scanner s = new Scanner(System.in);
            int result = s.nextInt(); // o primeiro valor inteiro que encontrar
            option = Option.values()[result]; // opção correspondente ao valor introduzido pelo usuário.
        }catch(RuntimeException e) {
            System.out.println("Erro");
            // nothing to do
        }
        return option;
    }

    private static void clearConsole() throws Exception {

        for(int y = 0; y < 25;y++){ // console window has 25 lines.
            System.out.println("\n");
        }

    }

    private void Login() throws java.sql.SQLException {

        Connection con = DriverManager.getConnection(getConnectionString());
        if(con != null)
            con.close();

    }

    public void Run() throws Exception {

        Login(); // estabelecer a ligação
        Option userInput; // input do usuário

        do{
            clearConsole();
            userInput = DisplayMenu(); // a DisplayMenu retorna a opção introduzida pelo usuário.
            clearConsole();
            try{

                __dbMethods.get(userInput).doWork();
                System.in.read(); // acho que: colocar a consola á espera do input do usuário.

            }catch(NullPointerException ex){
                //Nothing to do. The option was not a valid one. Read another.
            }

        }while(userInput!=Option.Exit);

    }

    public String getConnectionString() {
        return __connectionString;
    }

    public void setConnectionString(String s){
        __connectionString = s;
    }

    private void registerPessoa() {

        String values = Model.inputData("PESSOA(id,Noident,NIF,nproprio,apelido,morada,CodPostal,localidade,atrdisc)");
        System.out.println(values); // debug purposes.

        Pessoa pessoa = new Pessoa(values);
        Model.registerPessoa(pessoa);
    }

    private void registerCondutor() {// mostrar todas as pessoas que são condutores.(mesma coisa que proprietario)
        Model.validPessoa("condutor","C"); // displays valid pessoas to add to condutor.

        String values = Model.inputData("CONDUTOR(idPessoa,nccondução,dtnascimento)");
        System.out.println(values); // debug purposes.

        Condutor condutor = new Condutor(values);
        Model.registerCondutor(condutor);
    }

    private void registerProprietario() {
        Model.validPessoa("proprietario","P");

        String values = Model.inputData("PROPRIETARIO(idPessoa,dtnascimento)");
        System.out.println(values);

        Proprietario proprietario = new Proprietario(values);
        Model.registerProprietario(proprietario);

    }

    private void registerVeiculo() {

        String values = Model.inputData("VEICULO(id,matricula,tipo,modelo,marca,ano,proprietario)");
        System.out.println(values);

        Veiculo veiculo = new Veiculo(values);
        Model.registerVeiculo(veiculo); 

    }
}

public class Ap23{
    public static void main(String[] args) throws Exception{

        String url = "jdbc:postgresql://10.62.73.73:5432/?user=mp23&password=mp23&ssl=false";
        App.getInstance().setConnectionString(url);
        App.getInstance().Run();
    }
}