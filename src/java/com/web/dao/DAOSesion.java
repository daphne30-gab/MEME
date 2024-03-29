/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.dao;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.dao.domain.Sesion;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author amdza
 */
public class DAOSesion extends conexion  {
    public String autenticacion(String usuario,String contrasena){
       Sesion s = new Sesion();
       PreparedStatement pst=null;
       ResultSet rs=null;
       String nivel="";
       try{ 
           String consulta = "select CorreoUser, TipoUser from usuario where CorreoUser = ? and  PasswordUser = ? ";
           pst = getConexion().prepareStatement(consulta);
           pst.setString(1, usuario);
           pst.setString(2, contrasena);
           rs= pst.executeQuery();
           if(rs.next()){
                   s.setCorreo(rs.getString("CorreoUser"));
                   nivel=rs.getString("TipoUser");   }
           if(rs.absolute(1)){
               return nivel;
           }   
       }catch(Exception ex){  
        System.err.println("error");
       }finally{
           try{
           if(getConexion() !=null) getConexion().close();
           if(pst != null) pst.close();
           if(rs != null) rs.close(); 
           }catch(Exception ex){
                    System.err.println("error");
           }
       }
        return nivel;
    }
    
    public boolean registrar(Sesion s){
        
        PreparedStatement pst= null;
        
        try{
            String consulta="insert into usuario(NombreUser, ApellidoUser, CorreoUser, PasswordUser , TipoUser, EstatusUser) values(?,?,?,?,?,?)";
            pst = getConexion().prepareStatement(consulta);
            pst.setString(1, s.getNombre());
            pst.setString(2, s.getApellido());
            pst.setString(3, s.getCorreo());
            pst.setString(4, s.getPassword());
            pst.setString(5, "ALUMNO");
            pst.setString(6, "A");
            
            if(pst.executeUpdate() == 1){
                return true;
            }
            
        }catch(Exception e){
            
        }finally{
            try{
            if(getConexion() != null) getConexion().close();
            if(pst != null) pst.close();;
            
            }catch(Exception e){
                System.err.println("error"+ e);
                
            } 
            
        }
        
        return false;
        
    }
    public Sesion buscarSesion(String correo){
       Sesion s = new Sesion();
       Connection con=null;
       ResultSet rs=null;
       PreparedStatement ps= null;   
       try{
           String sql="SELECT * FROM usuarios WHERE  correo = ?";
           con=conexion.getConexion();
           if(con!=null){
               ps=con.prepareStatement(sql);
               ps.setString(1,correo);
               rs = ps.executeQuery();
               if(rs.next()){
                    s.setIdusuario(rs.getInt(1));
                    s.setNombre(rs.getString(2));
                    s.setApellido(rs.getString(3));
                    s.setCorreo(rs.getString(4));
                    s.setPassword(rs.getString(5));
                    s.setTipo(rs.getString(6));
               }
           }
           
       }catch(SQLException sqle){
           System.err.print("Error en la busqueda "+sqle.getMessage());
       }
       return s;
    }
    
    public ArrayList<Sesion> obtenerUsuarios(){
       ArrayList<Sesion> lista=new ArrayList<Sesion>();
       Connection con=null;
       PreparedStatement ps=null;
       String sql="SELECT * FROM usuario where EstatusUser='A'";
       try{
           con=conexion.getConexion();
           if(con!=null){
               ps=con.prepareStatement(sql);
               ResultSet rs= ps.executeQuery();
               while(rs.next()){
                    Sesion s = new Sesion();
                    s.setIdusuario(rs.getInt("IDUsuario"));
                    s.setCorreo(rs.getString("CorreoUser"));
                    s.setNombre(rs.getString("NombreUser"));
                    s.setApellido(rs.getString("ApellidoUser"));
                    s.setPassword(rs.getString("PasswordUser"));
                    s.setTipo(rs.getString("TipoUser"));
                    s.setCurso_idCurso(rs.getInt("Curso_IDCurso"));
                    lista.add(s);
                    System.out.println(lista);
               }
           }
       }catch(SQLException e){
            System.err.print("Error en la busqueda "+e.getMessage());
       }
       return lista;
    }
}
