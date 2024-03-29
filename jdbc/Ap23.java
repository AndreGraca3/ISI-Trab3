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
        registerVeiculo, // 5
        listClientsWithMostTrips, // 6
        listDriversWithNoTrips, // 7
        countOwnerCarsTrips, // 8
        getMostProfitableDriver, // 9
        custoTotalVeiculo,
        nTotalViagens
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
        __dbMethods.put(Option.listClientsWithMostTrips, new DbWorker() {public void doWork() {App.this.listClientsWithMostTrips();}});
        __dbMethods.put(Option.listDriversWithNoTrips, new DbWorker() {public void doWork() {App.this.listDriversWithNoTrips();}});
        __dbMethods.put(Option.countOwnerCarsTrips, new DbWorker() {public void doWork() {App.this.countOwnerCarsTrips();}});
        __dbMethods.put(Option.getMostProfitableDriver, new DbWorker() {public void doWork() {App.this.getMostProfitableDriver();}});
        __dbMethods.put(Option.custoTotalVeiculo, new DbWorker() {public void doWork() {App.this.custoTotalVeiculo();}});
        __dbMethods.put(Option.nTotalViagens, new DbWorker() {public void doWork() {App.this.nTotalViagens();}});
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
            System.out.println("Company Management");
            System.out.println();
            System.out.println("1. Exit");
            System.out.println("2. Register Condutor");
            System.out.println("3. Register Proprietario");
            System.out.println("4. Register Cliente");
            System.out.println("5. Register veiculo");
            System.out.println("6. List clients with most trips in a Year");
            System.out.println("7. List drivers with no trips");
            System.out.println("8. Count cars trips for a specific owner");
            System.out.println("9. Get most profitable driver");
            System.out.println("10. get total cost of a vehicle");
            System.out.println("11. get total number of trips of a vehicle");
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

        Model.changeValidTypes();
        Model.deleteOnCascade();

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

        int id = Model.getNextId("id","pessoa");

        String condutorValues;
        String pessoaValues;
    
        if(splitedValues.length != 9 && splitedValues.length != 3){
            System.out.println("Not a valid amout of values introduced! introduced: " + splitedValues.length);
            System.out.println("Expected 3 (add) or 9 (new).");
            System.out.println("Want to try again?(Y/N)");
            Scanner s = new Scanner(System.in);
            char answer = s.next().charAt(0);
            try{
                if(answer == 'Y' || answer == 'y'){
                    clearConsole();
                    registerCondutor();
                }
            }catch(Exception e) {
                    //nothing to do;
            } 
        } 

        if(splitedValues.length == 9){ // adding to PESSOA and Condutor.
                            //id           Ncconducao             dtnascimento                                    
            condutorValues = id + "," + splitedValues[7] + "," + splitedValues[8];
                            //id            noident                 NIF                     nproprio                    apelido                 morada                  codPostal               localidade
            pessoaValues = id + "," + splitedValues[0] + "," + splitedValues[1] + "," + splitedValues[2] + "," + splitedValues[3] + "," + splitedValues[4] + "," + splitedValues[5] + "," + splitedValues[6] + "," + "C";

            Condutor condutor = new Condutor(condutorValues);
            Pessoa pessoa = new Pessoa(pessoaValues);

            Model.registerPessoa(pessoa);
            Model.registerCondutor(condutor);
        }

        if(splitedValues.length == 3){ // already exists in PESSOA, just need info for Condutor.
            condutorValues = values;

            Condutor condutor = new Condutor(condutorValues);

            Model.registerCondutor(condutor);
        }

    }

    private void registerProprietario() {
        Model.showValidPessoa();

        String values = Model.inputData("new => Identification number, NIF, first name, last name, address, phone number, region, birthdate.\nAlready registered => id from the table above and birthdate.");

        String[] splitedValues = values.split(",");
        
        int id = Model.getNextId("id","pessoa");

        String proprietarioValues;
        String pessoaValues;

        if(splitedValues.length != 8 && splitedValues.length != 2){
            System.out.println("Not a valid amout of values introduced! introduced: " + splitedValues.length);
            System.out.println("Expected 2 (add) or 8 (new).");
            System.out.println("Want to try again?(Y/N)");
            Scanner s = new Scanner(System.in);
            char answer = s.next().charAt(0);
            try{
                if(answer == 'Y' || answer == 'y'){
                    clearConsole();
                    registerProprietario();
                }
            }catch(Exception e) {
                    //nothing to do;
            } 
        } 

        if(splitedValues.length == 8){ // adding to PESSOA and PROPRIETARIO.
                            //id          dtnascimento                                    
            proprietarioValues = id + "," + splitedValues[7];
                            //id            noident                 NIF                     nproprio                    apelido                 morada                  codPostal               localidade
            pessoaValues = id + "," + splitedValues[0] + "," + splitedValues[1] + "," + splitedValues[2] + "," + splitedValues[3] + "," + splitedValues[4] + "," + splitedValues[5] + "," + splitedValues[6] + "," + "C";

            Proprietario proprietario = new Proprietario(proprietarioValues);
            Pessoa pessoa = new Pessoa(pessoaValues);

            Model.registerPessoa(pessoa);
            Model.registerProprietario(proprietario);
        }

        if(splitedValues.length == 2){ // already exists in PESSOA, just need info for PROPRIETARIO.
            proprietarioValues = values;

            Proprietario proprietario = new Proprietario(proprietarioValues);

            
            Model.registerProprietario(proprietario); // verifico que não estou a fazer nada de errado.
        }
    }

    private void registerCliente() {

        String values = Model.inputData("Identification number, NIF, first name, last name, address, phone number, region");

        String[] splitedValues = values.split(",");

        if(splitedValues.length != 7){
            System.out.println("Not a valid amout of values introduced! introduced: " + splitedValues.length);
            System.out.println("Expected 7(new).");
            System.out.println("Want to try again?(Y/N)");
            Scanner s = new Scanner(System.in);
            char answer = s.next().charAt(0);
            try{
                if(answer == 'Y' || answer == 'y'){
                    clearConsole();
                    registerCliente();
                }
            }catch(Exception e) {
                    //nothing to do;
            } 
        }

        if(splitedValues.length == 7){
            String clienteValues = Model.getNextId("id","pessoa") + "," + values + "," + "CL";

            Pessoa cliente = new Pessoa(clienteValues);
            Model.registerPessoa(cliente);
        }
    }

    private void registerVeiculo() {
        Model.printResult("SELECT id, ( nproprio || ' ' || apelido) as nome, dtNascimento from proprietario p1 inner join pessoa p2 on p1.idpessoa = p2.id");

        String values = Model.inputData("license plate, Number of seats, multiplier, designation, model, brand, year, color, owner");
       
        String[] splitedValues = values.split(",");

        if(splitedValues.length != 9){
            System.out.println("Not a valid amout of values introduced! introduced: " + splitedValues.length);
            System.out.println("Expected 6.");
            System.out.println("Want to try again?(Y/N)");
            Scanner s = new Scanner(System.in);
            char answer = s.next().charAt(0);
            try{
                if(answer == 'Y' || answer == 'y'){
                    clearConsole();
                    registerVeiculo();
                }
            }catch(Exception e) {
                //nothing to do;
            } 
        }   

        if(splitedValues.length == 9){
            int owner_id = Integer.parseInt(splitedValues[8]);
            int nextId = Model.getNextId("id","veiculo"); 
            boolean licencePlatePatter = 
                Model.verifyWithRegex("(?:\\d{2}[A-Z]{2}\\d{2})|(?:\\d{2}[A-Z]{2}\\d{2})",splitedValues[0]);

            int type_id = Model.verifyType(Integer.parseInt(splitedValues[1]),Integer.parseInt(splitedValues[2]),splitedValues[3]);

            String veiculoValues = nextId + "," + splitedValues[0] + "," + type_id + "," + splitedValues[4] + "," + splitedValues[5] + "," + splitedValues[6] + "," + splitedValues[8];
            Veiculo veiculo = new Veiculo(veiculoValues);

            String corValues = nextId + "," + splitedValues[7];
            Cor_veiculo cor = new Cor_veiculo(corValues);

            try{  
                if(!licencePlatePatter) throw new Exception("Not a valid licence_plate.");
                                      //owner
                if(Model.owns20vehicles(owner_id) == true){
                    System.out.println("You are trying to add a new vehicle to a owner wich already owns 20 vehicles");
                    System.out.println("Adding this new vehicle will remove the older vehicle owned by this owner!");
                    System.out.println("(The first ever vehicle register for this owner.)");
                    System.out.println("Do you want to add this new vehicle?(Y/N)");
                    Scanner s = new Scanner(System.in);
                    char answer = s.next().charAt(0); 
                    if(answer == 'Y' || answer == 'y'){    
                        Model.removeAndUpdateVeiculo(veiculo,owner_id);
                    }
                }else{
                    Model.registerVeiculo(veiculo);
                }
                Model.registerCor(cor);
            } catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void listClientsWithMostTrips() {
        String year = Model.inputData("Year");
        Model.listClientsWithMostTrips(year); 
    }

    private void listDriversWithNoTrips() {
        Model.listDriversWithNoTrips();
    }

    private void countOwnerCarsTrips() {
        String values = Model.inputData("Name/NIF, Year");
        String[] splitedValues = values.split(",");
        Model.countOwnerCarsTrips(splitedValues[0], Integer.parseInt(splitedValues[1]));
    }

    private void getMostProfitableDriver() {
        String year = Model.inputData("Year");
        Model.getMostProfitableDriver(year);
    }

    private void custoTotalVeiculo() {
        Model.printResult("SELECT id,matricula,modelo, marca from veiculo");
        String idveiculo = Model.inputData("Vehicle's id");
        Model.custoTotalVeiculo(Integer.parseInt(idveiculo));
    }

    private void nTotalViagens(){
        Model.printResult("SELECT id,matricula,modelo, marca from veiculo");
        String idveiculo = Model.inputData("Vehicle's id");
        Model.custoTotalVeiculo(Integer.parseInt(idveiculo));
    }

}

public class Ap23{
    public static void main(String[] args) throws Exception{

        String url = "jdbc:postgresql://10.62.73.73:5432/?user=mp23&password=mp23&ssl=false";
        App.getInstance().setConnectionString(url);
        App.getInstance().Run();
    }
}



//[A-z]{2}-[0-9]{7}