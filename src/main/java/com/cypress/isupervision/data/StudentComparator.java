package com.cypress.isupervision.data;

import com.cypress.isupervision.data.entity.user.Student;

import java.util.Comparator;

public class StudentComparator implements Comparator<Student>
{
        public int compare(Student student1, Student student2)
        {
            String studentName1=student1.getFirstname().toUpperCase();
            String studentName2=student2.getFirstname().toUpperCase();

            return studentName1.compareTo(studentName2);
        }
}
