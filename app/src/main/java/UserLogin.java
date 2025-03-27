public class UserLogin {
    public class UserInfo {
       public String userId;
        private String username;
        private String email;

        // Empty constructor (Required by Firebase)
        public void User() {
        }

        // Constructor with parameters
        public UserInfo(String userId, String username, String email) {
            this.userId = userId;
            this.username = username;
            this.email = email;
        }

        // Getters and Setters (Required for Firebase to map data)
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

}
