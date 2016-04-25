package finalyear.officeme;

import finalyear.officeme.model.User;

interface GetUserCallback {

    /**
     * Invoked when background task is completed
     */

    public abstract void done(User returnedUser);
}
