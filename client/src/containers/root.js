import React, { Suspense } from 'react';
import axios from 'axios';
import { Route } from 'react-router';
import { Provider } from 'react-redux';
import { ConnectedRouter } from 'connected-react-router';
import { ThemeProvider } from '@mui/material/styles';

import Toast from '../components/toast/index';
import Spinner from '../components/spinner/index';
import { Theme } from './../styles/theme';
import { isNullOrEmptyString } from '../utils/validations';
import {
    ROOT_URL,
    AUTH_URL,
    MAIN_URL
} from '../config/urls';

const Auth = React.lazy(() => import('./auth/index'));
const Main = React.lazy(() => import('./main/index'));

const setInterceptors = config => {
    const state = store.getState();
    const token = state && state.authReducer && state.authReducer.token ? state.authReducer.token : '';

    if (!isNullOrEmptyString(token)) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }

    return config;
};

const Root = ({ store, history }) => {

    axios.interceptors.request.use(setInterceptors, e => console.log(e));

    return (
        <Provider store={store}>
            <ThemeProvider theme={Theme}>
                <Toast />
                <Spinner />
                <ConnectedRouter history={history}>
                    <Suspense fallback={<></>}>
                        <>
                            <Route exact path={ROOT_URL} component={Auth} />
                            <Route path={AUTH_URL} component={Auth} />
                            <Route path={MAIN_URL} component={Main} />
                        </>
                    </Suspense>
                </ConnectedRouter>
            </ThemeProvider>
        </Provider>
    );
};

export default Root;