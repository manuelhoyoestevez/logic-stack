import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import Box from '@mui/material/Box';

import * as langActions from '../../services/lang/actions';
import * as navActions from '../../services/nav/actions';
import { selectToken } from '../../services/auth/selectors';
import { selectLanguage } from '../../services/lang/selectors';
import { isNullOrEmptyString } from '../../utils/validations';
import { AUTH_URL } from '../../config/urls';
import * as styles from './styles';

const Main = ({ token, language, navActions, langActions }) => {

    useEffect(() => {
        if (isNullOrEmptyString(token)) {
            navActions.replace(AUTH_URL);
        }

        langActions.setTranslations({ language, refresh: false });
    });

    return (
        <Box sx={styles.container}>
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

export default connect(mapStateToProps, mapDispatchToProps)(Main);