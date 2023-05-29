package com.taskgrid.makebugsnotwar.service;

import org.springframework.stereotype.Service;
@Service
public class ControllerServices {
    public String checkFirstName(String firstName){
        String checkedName = firstName;
        checkedName = checkedName.substring(0,1).toUpperCase() + checkedName.substring(1).toLowerCase();
        return checkedName;
    }
    public String checkLastName(String lastName){
        String checkedName = lastName;
        checkedName = checkedName.substring(0,1).toUpperCase() + checkedName.substring(1).toLowerCase();
        return checkedName;
    }
    public boolean checkUsername(String username){
        boolean testUsername = false;
        String usernameOutput = "";

        while(!testUsername){
            char[] usernameChars = username.toCharArray();
            if(username.length() < 20){
                for(char aChar : usernameChars ) {
                    if(Character.isLetter(aChar) || Character.isDigit(aChar)
                            || aChar == 'Æ' || aChar == 'Ø' || aChar == 'Å'
                            || aChar == 'æ' || aChar == 'ø' || aChar == 'å'){
                        usernameOutput = usernameOutput.concat(Character.toString(aChar));
                        if(usernameOutput.length() == username.length()){
                            testUsername = true;
                        }
                    }
                    else{
                        return false;
                    }
                }
            }
            else{
                return false;
            }
        }
        return true;
    }
    public boolean checkPassword(String password){
        return password.length() < 345 && password.length() > 16;
    }
    public boolean checkEmail(String email){
        return email.contains("@") && email.contains(".");
    }

    public boolean confirmDelete(String input){

        return input.equals("DELETE");
    }
}


