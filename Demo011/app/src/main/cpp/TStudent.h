//
// Created by zTercel on 2019-12-22.
//
#include <string>

class TStudent {
private:
    std::string m_Name;
    int         m_Age;

public:
    static TStudent* getInstance() {
        static TStudent student;
        return &student;
    }

public:
    void setName(std::string name) {
        m_Name = name;
    }

    std::string getName() {
        return m_Name;
    }

    void setAge(int age) {
        m_Age = age;
    }

    int getAge() {
        return m_Age;
    }
};