package com.taskgrid.makebugsnotwar.service;

import org.springframework.stereotype.Service;
@Service
public class ControllerServices {
    public String checkFirstName(String firstName){ //Sets every character to lowercase except the first one, which is set to uppercase
        String checkedName = firstName;
        checkedName = checkedName.substring(0,1).toUpperCase() + checkedName.substring(1).toLowerCase();
        return checkedName;
    }
    public String checkLastName(String lastName){
        String checkedName = lastName;
        checkedName = checkedName.substring(0,1).toUpperCase() + checkedName.substring(1).toLowerCase();
        return checkedName;
    }
    public boolean checkUsername(String username){ //Checks if the username contains less than 20 characters and is made with letters or numbers
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
    public boolean checkPassword(String password){ //Checks the length of the password
        return password.length() < 345 && password.length() > 16;
    }
    public boolean checkEmail(String email){ //Checks if the email contains @ or .
        return email.contains("@") && email.contains(".");
    }

    public boolean confirmDelete(String input){ //Checks if DELETE is typed when trying to delete a project, board or user

        return input.equals("DELETE");
    }
}


