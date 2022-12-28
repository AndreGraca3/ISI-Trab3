



class Model {

    static void registerProprietario(proprietario prop){
        
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
    }

    static void registerCondutor(condutor cond){
        //note : only registes, doesn't verify a thing!
        final String INSERT_CMD = 
                "INSERT INTO condutor values(?,?,?) "; // (idPessoa,ncconducao,dtnascimento)

        try (
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt = con.prepareStatement(INSERT_CMD);
        ) {

            con.setAutoCommit(false);
            pstmt.setInt(1,cond.getIdpessoa());
            pstmt.setString(2,cond.getNcconducao());
            pstmt.setString(3,cond.getDtnascimento());
            
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
        System.out.print("Enter corresponding values, separated by commas, \n" + str); 
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
}