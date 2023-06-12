const promiseAllSequential = (promiseCreators, args = null, index = 0, results = []) => new Promise((resolve, reject) => {
    if (index !== parseInt(index)) {
        return reject(new Error(`Invalid index '${index}' that must be integer`));
    }

    if (index === promiseCreators.length) {
        return resolve(results);
    }

    if (index < 0 || index > promiseCreators.length) {
        return reject(new Error(`Invalid index '${index}': must be between 0 and ${promiseCreators.length - 1}`));
    }

    promiseCreators[index](results, index, args)
        .then(result => {
            results.push(result);
            return promiseAllSequential(promiseCreators, args, index + 1, results);
        })
        .then(resolve)
        .catch(reject);
});
