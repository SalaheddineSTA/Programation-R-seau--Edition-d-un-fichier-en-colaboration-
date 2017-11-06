/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project1;

/**
 *
 * @author saleh
 */
public class ProjectList {
    int id;
    String name;

    public ProjectList(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public String toString() {
        return  name ;
    }

    public int getId() {
        return id;
    }
    
}
