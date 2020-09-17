const validationFunctions = {
    validateLogin() {
        const { login } = this.state;
        let loginValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(login.length < 1) {
            loginValid = false;
            errorMsg.login = 'field-required'
        }
        else if(!/^([a-zA-Z0-9!@$^&*]+)$/.test(login)) {
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
    },

    validateRestaurantName() {
        const { name } = this.state;
        let nameValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(name.length < 1) {
            nameValid = false;
            errorMsg.name = 'field-required';
        }
        else if(!/^([a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ!@*,. -]+)$/.test(name)) {
            nameValid = false;
            errorMsg.name = 'field-pattern'
        }
        else if(name.length > 32) {
            nameValid = false;
            errorMsg.name = 'field-toolong'
        }
        this.setState({nameValid, errorMsg}, this.validateForm)
    },

    validateDescription() {
        const { description } = this.state;
        let descriptionValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(description.length < 1) {
            descriptionValid = false;
            errorMsg.description = 'field-required';
        }
        else if(!/^([a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ!@#$%^&*():/,.\n -]+)$/.test(description)) {
            descriptionValid = false;
            errorMsg.description = 'field-pattern'
        }
        this.setState({descriptionValid, errorMsg}, this.validateForm)
    },

    validateCity() {
        const { city } = this.state;
        let cityValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(city.length < 1) {
            cityValid = false;
            errorMsg.city = 'field-required';
        }
        else if(!/^([a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ -]+)$/.test(city)) {
            cityValid = false;
            errorMsg.city = 'field-pattern'
        }
        else if(city.length > 64) {
            cityValid = false;
            errorMsg.city = 'field-toolong'
        }
        this.setState({cityValid, errorMsg}, this.validateForm)
    },

    validateStreet() {
        const { street } = this.state;
        let streetValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(street.length < 1) {
            streetValid = false;
            errorMsg.street = 'field-required';
        }
        else if(!/^([a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ -]+)$/.test(street)) {
            streetValid = false;
            errorMsg.street = 'field-pattern'
        }
        else if(street.length > 64) {
            streetValid = false;
            errorMsg.street = 'field-toolong'
        }
        this.setState({streetValid, errorMsg}, this.validateForm)
    },

    validateStreetNo() {
        const { streetNo } = this.state;
        let streetNoValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(streetNo.length < 1) {
            streetNoValid = false;
            errorMsg.streetNo = 'field-required';
        }
        else if(!/^([0-9]+)$/.test(streetNo)) {
            streetNoValid = false;
            errorMsg.streetNo = 'field-pattern'
        }

        this.setState({streetNoValid, errorMsg}, this.validateForm)
    },

    validateDishName() {
        let { name } = this.state;
        let nameValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(name.length < 1) {
            nameValid = false;
            errorMsg.name = 'field-required';
        }
        else if(!/^([a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ!@$^&*,. -]+)$/.test(name)) {
            nameValid = false;
            errorMsg.name = 'field-pattern'
        }
        else if(name.length > 64) {
            nameValid = false;
            errorMsg.name = 'field-toolong'
        }

        this.setState({nameValid, errorMsg}, this.validateForm)
    },

    validateDishDescription() {
        let { description } = this.state;
        let descriptionValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(description.length < 1) {
            descriptionValid = false;
            errorMsg.description = 'field-required';
        }
        else if(!/^([a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ!@#$%^&*():,./\n -]+)$/.test(description)) {
            descriptionValid = false;
            errorMsg.description = 'field-pattern'
        }
        else if(description.length > 255) {
            descriptionValid = false;
            errorMsg.description = 'field-toolong'
        }

        this.setState({descriptionValid, errorMsg}, this.validateForm)
    },

    validateDishPrice() {
        let { price } = this.state;
        let priceValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(price.length < 1) {
            priceValid = false;
            errorMsg.price = 'field-required';
        }
        else if(!/^([0-9]{0,5}(,[0-9]{0,2})?)$/.test(price)) {
            priceValid = false;
            errorMsg.price = 'price-pattern'
        }

        this.setState({priceValid, errorMsg}, this.validateForm)
    }
};

export default validationFunctions;