const validationFunctions = {
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
    }
};

export default validationFunctions;