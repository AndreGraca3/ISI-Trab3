package jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

class Model {
    // criei 4 funções "parecidas" para registar cada utente (pessoa,condutor,proprietario e cliente)

    // PESSOA(id,Noident,NIF,nproprio,apelido,morada,CodPostal,localidade,atrdisc)

    // CONDUTOR(idPessoa,ncconducao,dtnascimento)

    // PROPRIETARIO(idPessoa,dtnascimento)

    // CLIENTE é a mesma coisa que PESSOA pois não tem atributos adicionais.

    static void registerPessoa(Pessoa pessoa){

        final String INSERT_CMD = 
                "INSERT INTO pessoa values(?,?,?,?,?,?,?,?,?)";

        try (
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt = con.prepareStatement(INSERT_CMD);
        ) {
            
            con.setAutoCommit(false);
            pstmt.setInt(1,pessoa.getId());
            pstmt.setString(2,pessoa.getNoident());
            pstmt.setString(3,pessoa.getNif());
            pstmt.setString(4,pessoa.getNproprio());
            pstmt.setString(5,pessoa.getApelido());
            pstmt.setString(6,pessoa.getMorada());
            pstmt.setInt(7,pessoa.getCodpostal());
            pstmt.setString(8,pessoa.getLocalidade());
            pstmt.setString(9,pessoa.getAtrdisc());

            pstmt.executeUpdate();
            con.commit();
            con.setAutoCommit(true);
            System.out.println("Pessoa registered!!!");
        }catch (SQLException e){
            e.getMessage();
            System.out.println("Error on insert values");
        }


    }

    /*static void registerProprietario(Proprietario prop){
        
        final String INSERT_CMD = 
                "INSERT INTO proprietario values(?,?) ";

        try (
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt = con.prepareStatement(INSERT_CMD);
        ) {
            
            con.setAutoCommit(false);
            pstmt.setInt(1,prop.getIdpessoa());
            pstmt.setString(2,cond.getDtnascimento());

            pstmt.executeUpdate();
            con.commit();
            con.setAutoCommit(true);
            System.out.println("Proprietario registered!!!");

        }catch (SQLException e){
            e.getMessage();
            System.out.println("Error on insert values");
        }
    }*/

    static void registerCondutor(Condutor cond){

        final String INSERT_CMD = 
                "INSERT INTO condutor values(?,?,?)"; // (idPessoa,ncconducao,dtnascimento)

        try (
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt = con.prepareStatement(INSERT_CMD);
        ) {

            con.setAutoCommit(false);
            pstmt.setInt(1,cond.getIdPessoa());
            pstmt.setString(2,cond.getNConducao());
            pstmt.setObject(3,cond.getDtNascimento());
            
            pstmt.executeUpdate();
            con.commit();
            con.setAutoCommit(true);
            System.out.println("Condutor registered!!!");

        }catch (SQLException e) {
            e.getMessage();
            System.out.println("Error on insert values");
        }
    }

    static String inputData(String str){
        java.util.Scanner key = new Scanner(System.in);
        System.out.println("Enter corresponding values, separated by commas, \n" + str); 
        String values = key.nextLine(); 
        return values;
    }

    static boolean owns20vehicles(ArrayList<Integer> vehicles, int id_prop){ // list of vehicles proprietario id's.
         // verifies if the given proprietario has 20 or more Veiculos.
        int count = 0;
        for(int v: vehicles) { // para cada id_prop na lista de veiculos
            if(v == id_prop) count++;
            if(count == 20) return true;
        }
        return false;
    }

    static boolean isCondutor(ArrayList<Integer> drivers, int id_prop){
        // verifies if the given proprietario is a condutor.
        for(int d: drivers) {
            if(d == id_prop) return true;
        }
        return false;
    }

    static boolean isProprietario(ArrayList<Integer> owners, int id_cond){
        // verifies if the given condutor is a proprietario.
        for(int p: owners){
            if(p == id_cond) return true;
        }
        return false;
    }

    static void validPessoa(String atrdisc) {

        final String SELECT_CMD = String.format(
        "(SELECT id,nproprio,apelido FROM pessoa WHERE atrdisc = ?) except (SELECT id,nproprio,apelido FROM pessoa p INNER JOIN condutor c on c.idpessoa = p.id)");
        
        try (
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt = con.prepareStatement(SELECT_CMD);
        ) {
            
            con.setAutoCommit(false);
            pstmt.setString(1,atrdisc);
            ResultSet res = pstmt.executeQuery();
            System.out.print("id   ");
            System.out.print("nproprio  ");
            System.out.println("apelido        ");
            while(res.next()){
                System.out.print(res.getInt("id"));
                int id_length = (Integer.toString(res.getInt("id"))).length();
                for(int steps = id_length ; steps < 5 ; steps++){
                    System.out.print(" ");
                }
                System.out.print(res.getString("nproprio"));
                int nproprio_length = (res.getString("nproprio")).length();
                for(int steps = nproprio_length; steps < 10 ; steps++){
                    System.out.print(" ");
                }
                System.out.println(res.getString("apelido"));
            }
            System.out.println("--------------------------------------------------------------------------------");
            
            con.commit(); //when several calls
            con.setAutoCommit(true);
            
        } catch (SQLException e) {
            e.getMessage();
            System.out.println("Error!!!");
        }
    }
}