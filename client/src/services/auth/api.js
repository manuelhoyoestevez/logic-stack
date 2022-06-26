import axios from 'axios';
import Config from '../../config/config';

export const loginApi = (body) => axios.post(
    Config.login,
    body,
    { headers: { 'Content-Type': 'application/json' } })
    .catch(e => {
        return { error: e, toast: { key: `auth.loginKO`, type: 'error' } };
    });
