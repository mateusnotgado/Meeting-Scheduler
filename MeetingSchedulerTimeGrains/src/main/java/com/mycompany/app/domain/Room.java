package com.mycompany.app.domain;

public class Room {
    int id;
    String name;
    int capacity;
public Room(){
    
}
    public Room(int id,String name, int capacity) {
        this.id=id;
        this.name = name;
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
}

