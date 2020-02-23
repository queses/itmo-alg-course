const { spawn } = require('child_process')
const { join } = require('path')
const { readFileSync } = require('fs')

const name = process.argv[2]
if (!name) {
    throw new Error('Task name should be provided')
}

spawn(
    'java',
    [ '-cp', join(__dirname, '../out/production/itmo-data-course'), name ],
    {
        cwd: join(__dirname, name),
        stdio: 'inherit'
    }
).on(
    'exit',
    (code) => {
        if (code === 0) {
            const outputBuffer = readFileSync(join(__dirname, name + '/output.txt'))
            console.log(outputBuffer.toString())
        }
    }
)
