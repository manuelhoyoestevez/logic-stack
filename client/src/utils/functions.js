export const dateFormat = (t) => {
    function format(m) {
        let f = new Intl.DateTimeFormat('en', m);
        return f.format(t);
    }
    return [{ year: 'numeric' }, { month: '2-digit' }, { day: '2-digit' }].map(format).join('-');
};

export const fileToBase64 = file => new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onloadend = () => resolve(reader.result.replace(/^data:.+;base64,/, ''));
    reader.onerror = error => reject(error);
    reader.readAsDataURL(file);
});