const { spawn } = require('child_process')
const { join } = require('path')
const { readFileSync } = require('fs')

let name = process.argv[2]
if (!name) {
    throw new Error('Task name should be provided')
}

const nameMatch = name.match(/^(\d+)[-._:](\d+$)/)
if (nameMatch) {
    name = 'Week' + nameMatch[1] + 'Task' + nameMatch[2]
}

const task = spawn(
    'java',
    [ '-Xss64M', '-Xmx512M', '-cp', join(__dirname, 'out/production/itmo-alg-course'), name ],
    { cwd: join(__dirname, 'resources/' + name), stdio: 'inherit' }
)

task.on('exit', (code) => {
    if (code === 0) {
        const outputBuffer = readFileSync(join(__dirname, 'resources/' + name + '/output.txt'))
        console.log(outputBuffer.toString())
    }
})
