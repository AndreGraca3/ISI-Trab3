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

    static void registerCondutor(Condutor cond){

        final String SELECT_CMD =
            "SELECT * FROM proprietario where idpessoa = ?"; 

        final String INSERT_CMD = 
                "INSERT INTO condutor values(?,?,?)"; // (idPessoa,ncconducao,dtnascimento)

        try (
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt1 = con.prepareStatement(SELECT_CMD);
            PreparedStatement pstmt2 = con.prepareStatement(INSERT_CMD);
        ) {

            con.setAutoCommit(false);

            pstmt1.setInt(1,cond.getIdPessoa());
            ResultSet rs = pstmt1.executeQuery();
            if(rs.getInt("idpessoa") == cond.getIdPessoa()) throw new SQLException("I am already a proprietario!");

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

    // not being used:
    /*static boolean isCondutor(ArrayList<Integer> drivers, int id_prop){
        // verifies if the given proprietario is a condutor.
        for(int d: drivers) {
            if(d == id_prop) return true;
        }
        return false;
    }*/

    /*static boolean isProprietario(ArrayList<Integer> owners, int id_cond){
        // verifies if the given condutor is a proprietario.
        for(int p: owners){
            if(p == id_cond) return true;
        }
        return false;
    }*/

    static void validPessoa(String table,String atrdisc) {

        final String SELECT_CMD = String.format(
        "(SELECT id,nproprio,apelido FROM pessoa WHERE atrdisc = ?) except (SELECT id,nproprio,apelido FROM pessoa p INNER JOIN %s on idpessoa = p.id)",table);
        
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
            
            con.commit();
            con.setAutoCommit(true);
            
        } catch (SQLException e) {
            e.getMessage();
            System.out.println("Error!!!");
        }
    }
}