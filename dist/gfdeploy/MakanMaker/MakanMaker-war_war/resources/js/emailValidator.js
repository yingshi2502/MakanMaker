
PrimeFaces.validator['custom.emailValidator'] = {

    pattern: /\S+@\S+/,

    validate: function (element, value) {
        //use element.data() to access validation metadata, in this case there is none.
        if (!this.pattern.test(value)) {
            throw {
                summary: 'Validation Error',
                detail: value + ' is not a valid email.'
            }
        }
    }
};