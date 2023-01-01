package jdbc;

class Veiculo {
private int id;
private String matricula;
private int tipo;
private String modelo;
private String marca;
private int ano;
private int proprietario;

Veiculo(String values){
    String [] atributos = values.split(",");
    id = Integer.parseInt(atributos[0]);
    matricula = atributos[1];
    tipo = Integer.parseInt(atributos[2]);
    modelo = atributos[3];
    marca = atributos[4];
    ano = Integer.parseInt(atributos[5]);
    proprietario = Integer.parseInt(atributos[6]);
}

public Integer getId(){return id;}
public String getMatricula(){return matricula;}
public Integer getTipo(){return tipo;}
public String getModelo(){return modelo;}
public String getMarca(){return marca;}
public Integer getAno(){return ano;}
public Integer getProprietario(){return proprietario;}

public void setId(Integer id){this.id = id;}
public void setMatricula(String matricula){this.matricula = matricula;}
public void setTipo(Integer tipo){this.tipo = tipo;}
public void setModelo(String modelo){this.modelo = modelo;}
public void setMarca(String marca){this.marca = marca;}
public void setAno(Integer ano){this.ano = ano;}
public void setProprietario(Integer proprietario){this.proprietario = proprietario;}
}