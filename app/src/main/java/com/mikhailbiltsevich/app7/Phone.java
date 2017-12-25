package com.mikhailbiltsevich.app7;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Phone implements Comparable<Phone>, Parcelable {
    private long id;
    private String firstname, surname, patronymic, address, creditCard;
    private double timeLongDistanceCalls, timeCityCalls, debit;

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public void setTimeLongDistanceCalls(double timeLongDistanceCalls) {
        this.timeLongDistanceCalls = timeLongDistanceCalls;
    }

    public void setTimeCityCalls(double timeCityCalls) {
        this.timeCityCalls = timeCityCalls;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public Phone(){

    }

    public Phone(long id, String firstname, String surname, String patronymic, String address, double timeLongDistanceCalls, double timeCityCalls, String creditCard, double debit) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.patronymic = patronymic;
        this.address = address;
        this.timeLongDistanceCalls = timeLongDistanceCalls;
        this.timeCityCalls = timeCityCalls;
        this.creditCard = creditCard;
        this.debit = debit;
    }

    public Phone(Parcel in) {
        Object[] array = in.readArray(getClass().getClassLoader());
        this.id = (int)array[0];
        this.firstname = (String)array[1];
        this.surname = (String)array[2];
        this.patronymic = (String)array[3];
        this.address = (String)array[4];
        this.timeLongDistanceCalls = (double)array[5];
        this.timeCityCalls = (double)array[6];
        this.creditCard = (String)array[7];
        this.debit = (double)array[8];
    }

    public long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getAddress() {
        return address;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public double getDebit() {
        return debit;
    }

    public double getTimeLongDistanceCalls() {
        return timeLongDistanceCalls;
    }

    public double getTimeCityCalls() {
        return timeCityCalls;
    }

    @Override
    public String toString() {
        return String.format("ИД: %d\nФИО: %s %s %s\nАдрес: %s\nНомер кредитной карты:\n%s\nДебет: %f\nВремя внутригородских звонков:\n%f\nВремя междугородних звонков:\n%f", id, surname, firstname, patronymic, address, creditCard, debit, timeCityCalls, timeLongDistanceCalls);
    }

    @Override
    public int compareTo(@NonNull Phone phone) {
        return this.surname.compareTo(phone.surname);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeArray(new Object[] {id, firstname, surname, patronymic, address, timeLongDistanceCalls, timeCityCalls, creditCard, debit});
    }

    public static final Parcelable.Creator<Phone> CREATOR = new Parcelable.Creator<Phone>() {

        @Override
        public Phone createFromParcel(Parcel source) {
            return new Phone(source);
        }

        @Override
        public Phone[] newArray(int size) {
            return new Phone[size];
        }
    };
}