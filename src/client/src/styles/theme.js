import { createTheme } from '@material-ui/core/styles';

export const Colors = {
    primary: '#232d4b',
    secondary: '#005573',
    accent_red: '#f04641'
};

export const Theme =
    createTheme({
        palette: {
            primary: {
                main: Colors.primary
            },
            secondary: {
                main: Colors.secondary
            },
            error: {
                main: Colors.accent_red
            }
        }
    });