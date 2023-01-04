package jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.SQLException;

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

    static void registerProprietario(Proprietario prop){

        final String SELECT_CMD = 
                "SELECT * FROM condutor where idpessoa = ?";
        
        final String INSERT_CMD = 
                "INSERT INTO proprietario values(?,?) ";

        try (
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt1 = con.prepareStatement(SELECT_CMD);
            PreparedStatement pstmt2 = con.prepareStatement(INSERT_CMD);
        ) {
            
            con.setAutoCommit(false);

            pstmt1.setInt(1,prop.getIdPessoa());
            ResultSet rs = pstmt1.executeQuery();
            if(rs.getInt("idpessoa") == prop.getIdPessoa()) throw new SQLException("I am already a proprietario!");


            pstmt2.setInt(1,prop.getIdPessoa());
            pstmt2.setObject(2,prop.getDtNascimento());

            pstmt2.executeUpdate();
            con.commit();
            con.setAutoCommit(true);
            System.out.println("Proprietario registered!!!");

        }catch (SQLException e){
            System.out.println(e.getMessage());
            //System.out.println("Error on insert values");
        }
    }

    static void registerCondutor(Pessoa pessoa,Condutor cond){


        final String INSERT_CMD_PESSOA = 
                "INSERT INTO pessoa values(?,?,?,?,?,?,?,?,?)"; // (id,noident,NIF,nproprio,apelido,morada,codpostal,localidade, atrdisc)

        final String INSERT_CMD_CONDUTOR = 
                "INSERT INTO condutor values(?,?,?)"; // (noident,NIF,nproprio,apelido,morada,
                //codpostal,localidade,ncconducao,dtnascimento)

        try (
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt1 = con.prepareStatement(INSERT_CMD_PESSOA);
            PreparedStatement pstmt2 = con.prepareStatement(INSERT_CMD_CONDUTOR);
        ) {

            con.setAutoCommit(false);

            pstmt1.setInt(1,pessoa.getId()); // id
            pstmt1.setString(2,pessoa.getNoident()); // noident
            pstmt1.setString(3,pessoa.getNif()); // nif
            pstmt1.setString(4,pessoa.getNproprio()); // nproprio
            pstmt1.setString(5,pessoa.getApelido()); // apelido
            pstmt1.setString(6,pessoa.getMorada()); // morada
            pstmt1.setInt(7,pessoa.getNtelefone()); // ntelefone
            pstmt1.setString(8,pessoa.getLocalidade()); // localidade
            pstmt1.setString(9,pessoa.getAtrdisc()); // atrdisc

            pstmt1.executeUpdate();
            
            pstmt2.setInt(1,cond.getIdPessoa());
            pstmt2.setString(2,cond.getNConducao());
            pstmt2.setObject(3,cond.getDtNascimento());
            
            pstmt2.executeUpdate();
            con.commit();
            con.setAutoCommit(true);
            System.out.println("Condutor registered!!!");

        }catch (SQLException e) {
            System.out.println(e.getMessage());
            //System.out.println("Error on insert values");
        }
    }

    //static void registerCliente(Cliente cliente){todo()}

    static void registerVeiculo(Veiculo veiculo){

        final String SELECT_CMD =
            "select proprietario, count(proprietario) as N_veiculos from veiculo v group by proprietario";

        final String INSERT_CMD =
            "INSERT INTO veiculo values(?,?,?,?,?,?,?)"; // (id,matricula,tipo,modelo,marca,ano,proprietario)

        try(
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt1 = con.prepareStatement(SELECT_CMD);
            PreparedStatement pstmt2 = con.prepareStatement(INSERT_CMD);
        ) {
            
            con.setAutoCommit(false);
            ResultSet rs = pstmt1.executeQuery();
            if(owns20vehicles(rs,veiculo.getProprietario()) == false){
                throw new SQLException("You can't register me! I already own 20 vehicles!");
            }

            pstmt2.setInt(1,veiculo.getId());
            pstmt2.setString(2,veiculo.getMatricula());
            pstmt2.setInt(3,veiculo.getTipo());
            pstmt2.setString(4,veiculo.getModelo());
            pstmt2.setString(5,veiculo.getMarca());
            pstmt2.setInt(6,veiculo.getAno());
            pstmt2.setInt(7,veiculo.getProprietario());

            pstmt2.executeUpdate();
            con.commit();
            con.setAutoCommit(true);
            System.out.println("Veiculo registered!!!");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    static String inputData(String str){
        java.util.Scanner key = new Scanner(System.in);
        System.out.println("Enter corresponding values, separated by commas, \n" + str); 
        String values = key.nextLine(); 
        return values;
    }

    static boolean owns20vehicles(ResultSet vehicles, int id_prop){ // list of vehicles proprietario id's.
        try{
            while (vehicles.next()){
                if(vehicles.getInt("proprietario") == id_prop){
                    if(vehicles.getInt("N_veiculos") == 20){
                        return false;
                    }
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    //vai a tabela pessoa buscar o ultimo id.
    static int getNextId(){
        
        final String SELECT_CMD = String.format( 
                "SELECT MAX(id) FROM pessoa");

        int id = 0;

        try(
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt = con.prepareStatement(SELECT_CMD);
        ){
            con.setAutoCommit(false);

            ResultSet res = pstmt.executeQuery();
            if(res.next()){
                id = res.getInt("max") + 1;
            };

            con.commit();
            con.setAutoCommit(true);
            System.out.println(id);

        } catch (SQLException e){
            e.getMessage();
            System.out.println("Error!!!");
        }
        return id;
    }

    static void showValidPessoa(String table) {

        final String SELECT_CMD = String.format(
        "(select id,nproprio , apelido from pessoa where atrdisc = 'CL') except (SELECT id, nproprio, apelido FROM pessoa inner join %s c on id = idpessoa)",table);
        
        try (
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt = con.prepareStatement(SELECT_CMD);
        ) {
            
            con.setAutoCommit(false);
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
            
            con.commit();
            con.setAutoCommit(true);
            
        } catch (SQLException e) {
            e.getMessage();
            System.out.println("Error!!!");
        }
    }
}