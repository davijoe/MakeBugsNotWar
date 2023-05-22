package com.taskgrid.makebugsnotwar.service;

import org.springframework.stereotype.Service;
@Service
public class ControllerServices {
    public String checkFirstname(String firstname){
        String checkedName = firstname;
        checkedName = checkedName.substring(0,1).toUpperCase() + checkedName.substring(1).toLowerCase();
        return checkedName;
    }
    public String checkLastname(String lastname){
        String checkedName = lastname;
        checkedName = checkedName.substring(0,1).toUpperCase() + checkedName.substring(1).toLowerCase();
        return checkedName;
    }
    public boolean checkUsername(String username){
        boolean testUsername = false;
        String username_output = "";

        while(!testUsername){
            char[] username_chars = username.toCharArray();
            if(username.length() < 20){
                for(char aChar : username_chars ) {
                    if(Character.isLetter(aChar) || Character.isDigit(aChar)
                            || aChar == 'Æ' || aChar == 'Ø' || aChar == 'Å'
                            || aChar == 'æ' || aChar == 'ø' || aChar == 'å'){
                        username_output = username_output.concat(Character.toString(aChar));
                        if(username_output.length() == username.length()){
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
}
