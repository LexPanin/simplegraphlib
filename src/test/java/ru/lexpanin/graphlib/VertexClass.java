package ru.lexpanin.graphlib;

public class VertexClass {
    private String Name;
    private String Property;

    public VertexClass(String name) {
        Name = name;
        Property = name + " property";
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProperty() {
        return Property;
    }

    public void setProperty(String property) {
        Property = property;
    }

    public void userMethod(){
        Property = Property + "  method";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VertexClass VertexClass = (VertexClass) o;

        return Name.equals(VertexClass.Name);
    }

    @Override
    public int hashCode() {
        return Name.hashCode();
    }

    @Override
    public String toString() {
        return   Name + " " + Property;
    }
}