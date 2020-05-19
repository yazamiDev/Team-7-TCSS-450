package edu.uw.team7project.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserInfoSignInViewModel extends ViewModel {
    private final String mEmail;
    private final String mJwt;


    /**
     * A constructor for the User Info View model which takes a name and a json web token.
     *
     * @param email a string representing an email.
     * @param jwt a string representing a json web token
     */
    private UserInfoSignInViewModel(String email, String jwt) {

        //May want to store location information about users?
        mEmail = email;
        mJwt = jwt;

    }

    /**
     * A method for getting a used email.
     *
     * @return a String representing the user email.
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * A method for obtaining the json web token for the user.
     *
     * @return A string representing a json web token.
     */
    public String getJwt() { return mJwt; }


    /**
     * An inner class used to create user info View Models
     */
    public static class UserInfoSignInViewModelFactory implements ViewModelProvider.Factory {
        private final String email;
        private final String jwt;


        /**
         * A constructor for the User Info View model factory which takes a name and a
         * json web token.
         *
         * @param email a string representing an email.
         * @param jwt a string representing a json web token
         */
        public UserInfoSignInViewModelFactory(String email, String jwt) {
            this.email = email;
            this.jwt = jwt;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoSignInViewModel.class) {
                return (T) new UserInfoSignInViewModel(email, jwt);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + UserInfoSignInViewModel.class);
        }
    }
}
