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
            if(pessoa.getAtrdisc() == "CL"){
                System.out.println("Cliente registered!!!");
            }else System.out.println("Pessoa registered!!!");
            }catch (SQLException e){
            e.getMessage();
            System.out.println("Error on insert values");
        }
    }

    static void registerProprietario(Proprietario prop){

        final String SELECT_CMD = 
                "SELECT idpessoa FROM condutor where idpessoa = ?";
        
        final String INSERT_CMD = 
                "INSERT INTO proprietario values(?,?) ";

        final String UPDATE_CMD = 
                "update pessoa set atrdisc = P where id = ?";

        try (
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt1 = con.prepareStatement(SELECT_CMD);
            PreparedStatement pstmt2 = con.prepareStatement(INSERT_CMD);
            PreparedStatement pstmt3 = con.prepareStatement(UPDATE_CMD);
        ) {
            
            con.setAutoCommit(false);

            pstmt1.setInt(1,prop.getIdPessoa());
            ResultSet rs = pstmt1.executeQuery();
            while(rs.next()){
                if(rs.getInt("idpessoa") == prop.getIdPessoa()) throw new SQLException("I am already a Condutor! So you can't register me as a Proprietario.");
            }
            
            pstmt2.setInt(1,prop.getIdPessoa());
            pstmt2.setObject(2,prop.getDtNascimento());

            pstmt2.executeUpdate(); // executamos primeiro este que é para não dar erro.

            pstmt3.setString(1,"P");
            pstmt3.setInt(2,prop.getIdPessoa());

            pstmt3.executeUpdate();
            con.commit();
            con.setAutoCommit(true);
            System.out.println("Proprietario registered!!!");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    static void registerCondutor(Condutor cond){

        final String SELECT_CMD = 
                "SELECT idpessoa FROM proprietario where idpessoa = ?";

        final String INSERT_CMD_CONDUTOR = 
                "INSERT INTO condutor values(?,?,?)"; // (noident,NIF,nproprio,apelido,morada,
                //codpostal,localidade,ncconducao,dtnascimento)

        final String UPDATE_CMD = 
                "update pessoa set atrdisc = C where id = ?";

        try (
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt1 = con.prepareStatement(SELECT_CMD);
            PreparedStatement pstmt2 = con.prepareStatement(INSERT_CMD_CONDUTOR);
            PreparedStatement pstmt3 = con.prepareStatement(UPDATE_CMD);
        ) {

            con.setAutoCommit(false);

            pstmt1.setInt(1,cond.getIdPessoa());
            ResultSet rs = pstmt1.executeQuery();
            while(rs.next()){
                 if(rs.getInt("idpessoa") == cond.getIdPessoa()) throw new SQLException("I am already a Proprietario! So you can't register me as a Condutor.");
            }
            
            pstmt2.setInt(1,cond.getIdPessoa());
            pstmt2.setString(2,cond.getNConducao());
            pstmt2.setObject(3,cond.getDtNascimento());
            
            pstmt2.executeUpdate();

            pstmt3.setInt(1,cond.getIdPessoa());

            pstmt3.executeUpdate();

            con.commit();
            con.setAutoCommit(true);
            System.out.println("Condutor registered!!!");

        }catch (SQLException e) {
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

    static void removeAndUpdateVeiculo(Veiculo veiculo,int prop_id){

        final String SELECT_CMD =
            "SELECT min(id) from veiculo where proprietario = ?"; // buscar o id do veiculo mais antigo

        final String DELETE_CMD = 
            "DELETE from veiculo where id = ?";

        final String INSERT_CMD = String.format(
            "INSERT INTO veiculo values(?,?,?,?,?,?,%d)",prop_id); // id,matricula,tipo,modelo,marca,ano,proprietario

        try(
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt1 = con.prepareStatement(SELECT_CMD);
            PreparedStatement pstmt2 = con.prepareStatement(DELETE_CMD);
            PreparedStatement pstmt3 = con.prepareStatement(INSERT_CMD);
        ){

            con.setAutoCommit(false);

            int RemovedVeiculoId = -1;

            pstmt1.setInt(1,prop_id);
            ResultSet rs = pstmt1.executeQuery();
            while(rs.next()){
               RemovedVeiculoId = rs.getInt("min");
            }

            pstmt2.setInt(1,RemovedVeiculoId);
            pstmt2.executeUpdate();

            pstmt3.setInt(1,veiculo.getId());
            pstmt3.setString(2,veiculo.getMatricula());
            pstmt3.setInt(3,veiculo.getTipo());
            pstmt3.setString(4,veiculo.getModelo());
            pstmt3.setString(5,veiculo.getMarca());
            pstmt3.setInt(6,veiculo.getAno());
            pstmt3.executeUpdate();

            con.commit();
            con.setAutoCommit(true);
            System.out.println("Veiculo registered!!!");
        }catch(SQLException e){
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

    static void countOwnerCarsTrips(String info, int year){
        String SELECT_CMD;
        try {
            Integer.parseInt(info);
            SELECT_CMD =
            String.format("select p1.nproprio, p1.nif,extract(year from p3.dtinicio) as ano,count(v1.id) as N_viagens from pessoa p1 inner join proprietario p2 on p1.nif  = '%s' inner join veiculo v1 on v1.proprietario = p2.idpessoa inner join periodoativo p3 on p3.veiculo = v1.id and extract(year from p3.dtinicio) = '%d' group by p1.nproprio, p1.nif,extract(year from p3.dtinicio)",info,year);
        } catch(NumberFormatException e) {
            SELECT_CMD =
            String.format("select p1.nproprio, p1.nif,extract(year from p3.dtinicio) as ano,count(v1.id) as N_viagens from pessoa p1 inner join proprietario p2 on p1.nproprio  = '%s' inner join veiculo v1 on v1.proprietario = p2.idpessoa inner join periodoativo p3 on p3.veiculo = v1.id and extract(year from p3.dtinicio) = '%d' group by p1.nproprio, p1.nif,extract(year from p3.dtinicio)",info,year);
          
        }
  
        try(
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt1 = con.prepareStatement(SELECT_CMD);
        ) {
            
            con.setAutoCommit(false);
            ResultSet rs = pstmt1.executeQuery();

             System.out.print("Nome                 ");
             System.out.print("NIF                 ");
             System.out.print("Ano              ");
             System.out.println("Viagens");

            while (rs.next()){
                System.out.print(rs.getString("nproprio"));

                int nproprio_length = (rs.getString("nproprio")).length();
                for(int steps = nproprio_length ; steps < 20 ; steps++){
                    System.out.print(" ");
                }

                System.out.print(rs.getInt("nif"));
                int nif_length = Integer.toString(rs.getInt("nif")).length();
                for(int steps = nif_length ; steps < 20 ; steps++){
                    System.out.print(" ");
                }

                System.out.print(rs.getInt("ano"));
                int ano_length = Integer.toString(rs.getInt("ano")).length();
                for(int steps = ano_length ; steps < 20 ; steps++){
                    System.out.print(" ");
                }

                System.out.println(rs.getInt("n_viagens"));
                int viagens_length = Integer.toString(rs.getInt("n_viagens")).length();
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

    static void printResult(String table){
        String SELECT_CMD = String.format("SELECT * FROM %s", table);
        final int TAB_SIZE = 8;
        try(
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt1 = con.prepareStatement(SELECT_CMD);
        ) {
            
            con.setAutoCommit(false);
            ResultSet rs = pstmt1.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnsCount = meta.getColumnCount();
            StringBuffer sep = new StringBuffer("\n");

            // This code is just demonstrative and only works for the two
            // columns existing in the table jdbcdemo
            for (int i = 1; i <= columnsCount; i++) {
                System.out.print(meta.getColumnLabel(i));
                System.out.print("\t");
                
                for (int j = 0; j < meta.getColumnDisplaySize(i)+TAB_SIZE; j++) {
                    sep.append('-');
               }
            }
            System.out.println(sep);
            // Step 4 - Get result
            while (rs.next()) {
                // It's not the best way to do it. But as in this case the result
                // is to be exclusively displayed on the console, the practical
                // result serves
                for (int i = 1; i <= columnsCount; i++) {
                    System.out.print(rs.getObject(i));
                    System.out.print("\t");
                }
                System.out.println();
            }
			System.out.println();

            con.commit();
            con.setAutoCommit(true);

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
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
            "SELECT id, nproprio, apelido from pessoa where atrdisc = 'CL'");

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

    static void changeValidTypes() {

        final String UPDATE_CMD =
                "";

        try(
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt = con.prepareStatement(UPDATE_CMD);
        ){

            con.setAutoCommit(false);

            pstmt.executeUpdate();

            con.commit();
            con.setAutoCommit(true);
        } catch( SQLException e){
            System.out.println(e.getMessage());
        }

    }

}