package javaFile.Table;

public class ClassesTable {
    String className;
    String categories;
    String available;
    int noOfStudent;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ClassesTable(String className, String categories, String available, int noOfStudent, int id) {
        this.className = className;
        this.categories = categories;
        this.available = available;
        this.noOfStudent = noOfStudent;
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public int getNoOfStudent() {
        return noOfStudent;
    }

    public void setNoOfStudent(int noOfStudent) {
        this.noOfStudent = noOfStudent;
    }




}
