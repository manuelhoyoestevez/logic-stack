import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import Box from '@mui/material/Box';

import * as langActions from '../../services/lang/actions';
import * as navActions from '../../services/nav/actions';
import { selectToken } from '../../services/auth/selectors';
import { selectLanguage } from '../../services/lang/selectors';
import LoginForm from '../../components/forms/auth/login-form/index';
import { isNullOrEmptyString } from '../../utils/validations';
import { MAIN_URL } from '../../config/urls';
import Logo from '../../assets/images/logo.png';
import * as styles from './styles';

const Auth = ({ token, language, navActions, langActions }) => {

    useEffect(() => {
        if (!isNullOrEmptyString(token)) {
            navActions.replace(MAIN_URL);
        }

        langActions.setTranslations({ language, refresh: false });
    });

    return (
        <Box sx={styles.container}>
            <Box sx={styles.logo}>
                <img src={Logo} width={300} />
            </Box>
            <LoginForm />
        </Box>
    );
}

function mapStateToProps(state) {
    return {
        token: selectToken(state),
        language: selectLanguage(state)
    };
}

function mapDispatchToProps(dispatch) {
    return {
        langActions: bindActionCreators(langActions, dispatch),
        navActions: bindActionCreators(navActions, dispatch)
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Auth);