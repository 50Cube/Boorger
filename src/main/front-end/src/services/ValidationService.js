const validationFunctions = {
    validateLogin() {
        const { login } = this.state;
        let loginValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(login.length < 1) {
            loginValid = false;
            errorMsg.login = 'field-required'
        }
        else if(!/^([a-zA-Z0-9!@#$%^&*]+)$/.test(login)) {
            loginValid = false;
            errorMsg.login = 'field-pattern'
        }
        else if(login.length > 32) {
            loginValid = false;
            errorMsg.login = 'field-toolong'
        }
        this.setState({loginValid, errorMsg}, this.validateForm)
    },


    validatePassword() {
        const {password} = this.state;
        let passwordValid = true;
        let errorMsg = {...this.state.errorMsg};

        if (password.length < 1) {
            passwordValid = false;
            errorMsg.password = 'field-required'
        } else if (password.length < 8) {
            passwordValid = false;
            errorMsg.password = 'password-length'
        } else if (!/\d/.test(password)) {
            passwordValid = false;
            errorMsg.password = 'password-number'
        } else if (!/[!@#$%^&*]/.test(password)) {
            passwordValid = false;
            errorMsg.password = 'password-char'
        } else if (password.length > 32) {
            passwordValid = false;
            errorMsg.password = 'field-toolong'
        }
        this.setState({passwordValid, errorMsg}, this.validateForm)
    },

    validateConfirmPassword() {
        const { confirmPassword } = this.state;
        let confirmPasswordValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(confirmPassword !== this.state.password) {
            confirmPasswordValid = false;
            errorMsg.confirmPassword = 'password-confirm';
        }
        this.setState({confirmPasswordValid, errorMsg}, this.validateForm)
    },

    validateFirstname(){
        const { firstname } = this.state;
        let firstnameValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(firstname.length < 1) {
            firstnameValid = false;
            errorMsg.firstname = 'field-required';
        }
        else if(!/^([a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+)$/.test(firstname)) {
            firstnameValid = false;
            errorMsg.firstname = 'field-pattern'
        }
        else if(firstname.length > 32) {
            firstnameValid = false;
            errorMsg.firstname = 'field-toolong'
        }
        this.setState({firstnameValid, errorMsg}, this.validateForm)
    },

    validateLastname() {
        const { lastname } = this.state;
        let lastnameValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(lastname.length < 1) {
            lastnameValid = false;
            errorMsg.lastname = 'field-required';
        }
        else if(!/^([a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ`-]+)$/.test(lastname)) {
            lastnameValid = false;
            errorMsg.lastname = 'field-pattern'
        }
        else if(lastname.length > 32) {
            lastnameValid = false;
            errorMsg.lastname = 'field-toolong'
        }
        this.setState({lastnameValid, errorMsg}, this.validateForm)
    },

    validateEmail() {
        const { email } = this.state;
        let emailValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(email.length < 1) {
            emailValid = false;
            errorMsg.email = 'field-required';
        } else if(!/^([a-zA-Z0-9-_]+@[a-zA-Z0-9]+\.[a-zA-Z]{2,})$/.test(email)) {
            emailValid = false;
            errorMsg.email = 'email-pattern';
        }
        else if(email.length > 32) {
            emailValid = false;
            errorMsg.email = 'field-toolong'
        }
        this.setState({emailValid, errorMsg}, this.validateForm)
    },


    validateAccessLevels() {
        const { adminChecked } = this.state;
        const { managerChecked } = this.state;
        const { clientChecked } = this.state;
        let accessLevelsValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(!adminChecked && !managerChecked && !clientChecked) {
            accessLevelsValid = false;
            errorMsg.accessLevels = 'emptyRoles'
        }
        this.setState({accessLevelsValid, errorMsg}, this.validateForm);
    }
};

export default validationFunctions;