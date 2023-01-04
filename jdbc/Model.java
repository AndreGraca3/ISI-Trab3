package jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.SQLException;

class Model {

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
            pstmt.setInt(7,pessoa.getNtelefone());
            pstmt.setString(8,pessoa.getLocalidade());
            pstmt.setString(9,pessoa.getAtrdisc());

            pstmt.executeUpdate();
            con.commit();
            con.setAutoCommit(true);
            System.out.println("Cliente registered!!!");
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

        final String INSERT_CMD_CONDUTOR = 
                "INSERT INTO condutor values(?,?,?)"; // (noident,NIF,nproprio,apelido,morada,
                //codpostal,localidade,ncconducao,dtnascimento)

        try (
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt = con.prepareStatement(INSERT_CMD_CONDUTOR);
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
            if(e.getMessage() == "insert or update on table 'condutor' violates foreign key constraint 'condutor_idpessoa_fkey"){
                System.out.println("There is no pessoa in the table above, it means you need to introduce a new condutor with all the info.");
            };
            System.out.println(e.getMessage());
            //System.out.println("Error on insert values");
        }
    }

    static void registerVeiculo(Veiculo veiculo){

        final String INSERT_CMD =
            "INSERT INTO veiculo values(?,?,?,?,?,?,?)"; // (id,matricula,tipo,modelo,marca,ano,proprietario)

        try(
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt = con.prepareStatement(INSERT_CMD);
        ) {

            con.setAutoCommit(false);

            pstmt.setInt(1,veiculo.getId());
            pstmt.setString(2,veiculo.getMatricula());
            pstmt.setInt(3,veiculo.getTipo());
            pstmt.setString(4,veiculo.getModelo());
            pstmt.setString(5,veiculo.getMarca());
            pstmt.setInt(6,veiculo.getAno());
            pstmt.setInt(7,veiculo.getProprietario());

            pstmt.executeUpdate();
            con.commit();
            con.setAutoCommit(true);
            System.out.println("Veiculo registered!!!");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    static void listClientsWithMostTrips(String year){
        final String SELECT_CMD =
            String.format("select nome_cliente, max(N_viagens) as Mais_viagens from(select id,(nproprio || ' ' || apelido) as nome_cliente,nif, count(c.idpessoa) as N_viagens from clienteviagem c inner join viagem v on viagem = idSistema inner join pessoa p on id = idpessoa where extract(year from v.dtviagem) = %s group by p.id) as A group by nome_cliente", year);

        try(
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt1 = con.prepareStatement(SELECT_CMD);
        ) {
            
            con.setAutoCommit(false);
            ResultSet rs = pstmt1.executeQuery();
            
            System.out.print("Nome              ");
            System.out.println("Viagens");

            while (rs.next()){
                System.out.print(rs.getString("nome_cliente"));

                int nome_cliente_length = (rs.getString("nome_cliente")).length();
                for(int steps = nome_cliente_length ; steps < 20 ; steps++){
                    System.out.print(" ");
                }

                System.out.println(rs.getInt("Mais_viagens"));
            }
           
            con.commit();
            con.setAutoCommit(true);

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    static void listDriversWithNoTrips(){
        final String SELECT_CMD =
            "select id,(nproprio || ' ' || apelido) as nome_condutor,nif from pessoa p1 inner join condutor C on id = idpessoa left outer join periodoativo p2 on idpessoa = condutor where condutor isnull;";

        try(
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt1 = con.prepareStatement(SELECT_CMD);
        ) {
            
            con.setAutoCommit(false);
            ResultSet rs = pstmt1.executeQuery();
            
             System.out.print("Nome                 ");
            System.out.println("NIF");

            while (rs.next()){
                System.out.print(rs.getString("nome_condutor"));

                int nome_condutor_length = (rs.getString("nome_condutor")).length();
                for(int steps = nome_condutor_length ; steps < 20 ; steps++){
                    System.out.print(" ");
                }

                System.out.println(rs.getInt("nif"));
            }
            con.commit();
            con.setAutoCommit(true);

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

    static boolean owns20vehicles(int id_prop){ // list of vehicles proprietario id's.

        final String SELECT_CMD =
            "select proprietario, count(proprietario) as N_veiculos from veiculo v group by proprietario";

        try(
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt1 = con.prepareStatement(SELECT_CMD);
        ){

            con.setAutoCommit(false);
            ResultSet rs = pstmt1.executeQuery();
            while (rs.next()){
                if(rs.getInt("proprietario") == id_prop){
                    if(rs.getInt("N_veiculos") == 20){
                        con.commit();
                        con.setAutoCommit(true);
                        return true;
                    }
                }
            }

            con.commit();
            con.setAutoCommit(true);

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    //vai a tabela pessoa buscar o ultimo id.
    static int getNextId(String table){
        
        final String SELECT_CMD = String.format( 
                "SELECT MAX(id) FROM %s",table);

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

        } catch (SQLException e){
            e.getMessage();
            System.out.println("Error!!!");
        }
        return id;
    }

    static void showValidPessoa() {

        final String SELECT_CMD = String.format(
            "(select id,nproprio , apelido from pessoa where atrdisc = 'CL') except (SELECT id, nproprio, apelido FROM pessoa p, condutor c, proprietario p2 where p.id = c.idpessoa or p2.idpessoa = p.id)");

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