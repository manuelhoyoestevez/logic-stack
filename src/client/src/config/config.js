import Settings from 'Settings';

const API = `${Settings.domain}:${Settings.port}${Settings.api}`;
const Config = {
    basename: Settings.basename,
    defaultLanguage: Settings.lang,

    login: `${API}${Settings.loginAPI}`
};

export default Config;