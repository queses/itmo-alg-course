const { spawn } = require('child_process')
const { join } = require('path')
const { readFileSync } = require('fs')

let name = process.argv[2]
if (!name) {
    console.log(
        'Usage: node run.js [TASK]\n' +
        'Examples: "node run.js Week1Task3", "node run.js 1-3"'
    )
    process.exit(0)
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
