package javaFile.Table;

public class ClassesTable {
    String className;
    String categories;
    String available;
    int noOfStudent;

    public ClassesTable(String className, String categories, String available, int noOfStudent) {
        this.className = className;
        this.categories = categories;
        this.available = available;
        this.noOfStudent = noOfStudent;
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
