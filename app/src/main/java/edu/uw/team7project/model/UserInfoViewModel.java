package edu.uw.team7project.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * A user Infor ViewModel used to store information about users to be used throughout
 * the application
 *
 * @author Trevor Nichols
 */
public class UserInfoViewModel extends ViewModel {
    private final String mEmail;
    private final String mJwt;

    /**
     * A constructor for the User Info View model which takes a name and a json web token.
     *
     * @param email a string representing an email.
     * @param jwt a string representing a json web token
     */
    private UserInfoViewModel(String email, String jwt) {
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
    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {
        private final String email;
        private final String jwt;

        /**
         * A constructor for the User Info View model factory which takes a name and a
         * json web token.
         *
         * @param email a string representing an email.
         * @param jwt a string representing a json web token
         */
        public UserInfoViewModelFactory(String email, String jwt) {
            this.email = email;
            this.jwt = jwt;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(email, jwt);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + UserInfoViewModel.class);
        }
    }
}
