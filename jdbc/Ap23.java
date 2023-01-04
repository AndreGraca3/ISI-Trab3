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
        registerCondutor, // 2
        registerProprietario, // 3
        registerCliente, // 4
        registerVeiculo // 5
    }

    private static App __instance = null;
    private String __connectionString;

    private HashMap<Option,DbWorker> __dbMethods; // instaciação do haspmap dos metodos da app.

    private App() {
        __dbMethods = new HashMap<Option,DbWorker>();
        __dbMethods.put(Option.registerCondutor, new DbWorker() {public void doWork() {App.this.registerCondutor();}});
        __dbMethods.put(Option.registerProprietario, new DbWorker() {public void doWork() {App.this.registerProprietario();}});
        __dbMethods.put(Option.registerCliente, new DbWorker() {public void doWork() {App.this.registerCliente();}});
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
            //Model.atualizar a lista de veiculos
            System.out.println("Company management");
            System.out.println();
            System.out.println("1. Exit");
            System.out.println("2. Register Condutor");
            System.out.println("3. Register Proprietario");
            System.out.println("4. Register Cliente");
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

    private void registerCondutor() {
        Model.showValidPessoa(); // displays valid pessoas to add to condutor.

        String values = 
            Model.inputData("new => Identification number, NIF, first name, last name, address, phone number, region, licence number, birthdate.\nAlready registered => id from the table above, licence number, birthdate.");

        String[] splitedValues = values.split(",");
        
        int id = Model.getNextId("pessoa");

        String condutorValues;
        String pessoaValues;

        if(values.length() == 9){ // adding to PESSOA and Condutor.
                            //id           Ncconducao             dtnascimento                                    
            condutorValues = id + "," + splitedValues[7] + "," + splitedValues[8];
                            //id            noident                 NIF                     nproprio                    apelido                 morada                  codPostal               localidade
            pessoaValues = id + "," + splitedValues[0] + "," + splitedValues[1] + "," + splitedValues[2] + "," + splitedValues[3] + "," + splitedValues[4] + "," + splitedValues[5] + "," + splitedValues[6] + "," + "C";

            Condutor condutor = new Condutor(condutorValues);
            Pessoa pessoa = new Pessoa(pessoaValues);

            Model.registerPessoa(pessoa);
            Model.registerCondutor(condutor);
        }

        if(values.length() == 3){ // already exists in PESSOA, just need info for Condutor.
            condutorValues = values;

            Condutor condutor = new Condutor(condutorValues);

            Model.registerCondutor(condutor);
        }

        if(values.length() != 9 || values.length() != 3){
            System.out.println("Not a valid amout of values introduced! introduced: " + values.length());
            System.out.println("Expected 3 (add) or 9 (new).");
            System.out.println("Want to try again?(Y/N)");
            Scanner s = new Scanner(System.in);
            char answer = s.next().charAt(0);
            try{
                clearConsole();
                if(answer == 'Y' || answer == 'y'){
                    registerCondutor();
                } else {
                    App.getInstance().Run();    
                } 
            }catch(Exception e) {
                    //nothing to do;
            } 
        } 
    }

    private void registerProprietario() {
        Model.showValidPessoa();

        String values = Model.inputData("new => Identification number, NIF, first name, last name, address, phone number, region, birthdate.\nAlready registered => id from the table above and birthdate.");

        String[] splitedValues = values.split(",");
        
        int id = Model.getNextId("pessoa");

        String proprietarioValues;
        String pessoaValues;

        if(values.length() == 8){ // adding to PESSOA and PROPRIETARIO.
                            //id          dtnascimento                                    
            proprietarioValues = id + "," + splitedValues[7];
                            //id            noident                 NIF                     nproprio                    apelido                 morada                  codPostal               localidade
            pessoaValues = id + "," + splitedValues[0] + "," + splitedValues[1] + "," + splitedValues[2] + "," + splitedValues[3] + "," + splitedValues[4] + "," + splitedValues[5] + "," + splitedValues[6] + "," + "C";

            Proprietario proprietario = new Proprietario(proprietarioValues);
            Pessoa pessoa = new Pessoa(pessoaValues);

            Model.registerPessoa(pessoa);
            Model.registerProprietario(proprietario);
        }

        if(values.length() == 2){ // already exists in PESSOA, just need info for PROPRIETARIO.
            proprietarioValues = values;

            Proprietario proprietario = new Proprietario(proprietarioValues);

            Model.registerProprietario(proprietario);
        }

        if(values.length() != 8 || values.length() != 2){
            System.out.println("Not a valid amout of values introduced! introduced: " + values.length());
            System.out.println("Expected 2 (add) or 8 (new).");
            System.out.println("Want to try again?(Y/N)");
            Scanner s = new Scanner(System.in);
            char answer = s.next().charAt(0);
            try{
                clearConsole();
                if(answer == 'Y' || answer == 'y'){
                    registerProprietario();
                } else {
                    App.getInstance().Run();    
                } 
            }catch(Exception e) {
                    //nothing to do;
            } 
        } 
    }

    private void registerCliente() {

        String values = Model.inputData("Identification number, NIF, first name, last name, address, phone number, region");
        System.out.println(values); // debug purposes.

        String clienteValues = Model.getNextId("pessoa") + "," + values + "," + "CL";

        Pessoa cliente = new Pessoa(clienteValues);
        Model.registerPessoa(cliente);
    }

    private void registerVeiculo() {

        String values = Model.inputData("license plate, type, model, brand, year, owner");
       
        String[] splitedValues = values.split(",");

        if(splitedValues.length != 6){
            System.out.println("Not a valid amout of values introduced! introduced: " + values.length());
            System.out.println("Expected 6.");
            System.out.println("Want to try again?(Y/N)");
            Scanner s = new Scanner(System.in);
            char answer = s.next().charAt(0);
            try{
                clearConsole();
                if(answer == 'Y' || answer == 'y'){
                    registerVeiculo();
                } else {
                    App.getInstance().Run();    
                } 
            }catch(Exception e) {
                //nothing to do;
            } 
        }   

        int owner_id = Integer.parseInt(splitedValues[5]);

        try{                        //owner
            if(Model.owns20vehicles(owner_id) == true){
                System.out.println("You are trying to add a new vehicle to a owner wich already owns 20 vehicles");
                System.out.println("Adding this new vehicle will remove the older vehicle owned by this owner!");
                System.out.println("Do you want to add this new vehicle?(Y/N)");
                Scanner s = new Scanner(System.in);
                char answer = s.next().charAt(0);
                if(answer == 'N' || answer == 'n'){
                    clearConsole();
                    
                }
            }
        } catch(Exception e){
            //nothing to do;
        }

        int nextId = Model.getNextId("veiculo");       

        String veiculoValues = nextId + "," + values;

        Veiculo veiculo = new Veiculo(veiculoValues);
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