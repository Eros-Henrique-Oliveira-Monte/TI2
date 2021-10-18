package com.ti2cc;

import java.sql.*;


public class DAO{

	Connection conexao;

	public DAO(){

		conexao = null;
	}

	public boolean conectar(){

		String driverName = "org.postgresql.Driver";
		String serverName = "localhost";
		String database = "dbase";
		int porta = 5432;
		String url = "jbdc:postgressql://" + serverName + ":" + porta + "/" + database;
		String user = "void";
		String pass = "pele123";
		boolean status = false;


		try{
			Class.forName(driverName);
			conexao = DriverManager.getConnection(url, user, pass);
			status = (conexao == null);
			System.out.println("Successful Connection");
		}catch(ClassNotFoundException e){
			System.err.println("Driver Not Found --" + e.getMessage());
		}catch(SQLException e){
			System.err.println("Failed Connection --" + e.getMessage());
		}

		return status;
	}


	public boolean close(){

		boolean status = false;

		try{
			conexao.close();
			status = true;
		}catch(SQLException e){
			System.err.println(e.getMessage());
		}

		return status;
	}


	public boolean insertUsuario(Usuario usuario){

		boolean status = false;

		try{
			Statement st = conexao.createStatement();
			st.executeUpdate("INSERT INTO usuario (id, login, senha, sexo )"
					+"VALUES ("+usuario.getId()+", '"+usuario.getLogin()+"', '"
					+usuario.getSenha()+"', '"+usuario.getSexo()+"');");
			st.close();
			status = true;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}

		return status;
	}

	public boolean updateUsuario(Usuario usuario){

		boolean status = false;

		try{
			Statement st = conexao.createStatement();
			st.executeUpdate("UPDATE usuario SET login = '"
					+usuario.getLogin()+"', senha = '"+usuario.getSenha()
					+"', sexo = '"+usuario.getSexo()+"'"+ " WHERE id = "+usuario.getId());
			st.close();
			status = true;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}

		return status;
	}


	public boolean deleteUsuario(Usuario usuario){

		boolean status = false;

		try{
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM usuario WHERE id = "+usuario.getId());
			st.close();
			status = true;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}

		return status;
	}

	public Usuario[] getUsuarios(){

		Usuario[] usuarios = null;

		try{
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM usuario");

			if(rs.next()){
				rs.last();
				usuarios = new Usuario[rs.getRow()];
				rs.beforeFirst();

				for(int i = 0; rs.next(); i++){
					usuarios[i] = new Usuario(rs.getInt("id"),rs.getString("login"),rs.getString("senha"),rs.getString("sexo").charAt(0));
				}
			}
			st.close();
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		return usuarios;
	}


	public Usuario[] getUsuariosMasculinos(){

		Usuario[] usuarios = null;

		try{
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM usuario WHERE usuario.sexo LIKE 'M'");

			if(rs.next()){
				rs.last();
				usuarios = new Usuario[rs.getRow()];
				rs.beforeFirst();

				for(int i = 0; rs.next(); i++){
					usuarios[i] = new Usuario(rs.getInt("id"),rs.getString("login"),rs.getString("senha"),rs.getString("sexo").charAt(0));
				}
			}
			st.close();
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		return usuarios;
	}
}





