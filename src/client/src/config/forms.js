var Forms = {
    auth: {
        fields: [
            {
                type: 'text',
                id: 'username',
                label: 'auth.username',
                placeholder: '',
                sx: '',
                className: 'MuiFilledInput-fix',
                validation: {
                    validators: ['isNullOrEmptyString'],
                    message: 'validations.required'
                },
                itemProps: {
                    xs: 12
                },
                inputProps: {
                    autoFocus: true,
                    fullWidth: true,
                    variant: 'filled',
                    autoComplete: 'off'
                },
                labelProps: {
                    shrink: true
                }
            },
            {
                type: 'password',
                id: 'password',
                label: 'auth.password',
                placeholder: '',
                sx: '',
                className: 'MuiFilledInput-fix',
                validation: {
                    validators: ['isNullOrEmptyString'],
                    message: 'validations.required'
                },
                itemProps: {
                    xs: 12
                },
                inputProps: {
                    fullWidth: true,
                    variant: 'filled'
                },
                labelProps: {
                    shrink: true
                },
                iconProps: {
                    position: 'start',
                    icon: 'check'
                }
            }
        ],
        actionsProps: {
            xs: 12,
            alignItems: 'center',
            justifyContent: 'center'
        }
    }
};