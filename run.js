const { spawn } = require('child_process')
const { join } = require('path')
const { readFileSync } = require('fs')

let name = process.argv[2]
let quietMode = false
if (name === '--quiet' || name === '-q') {
    quietMode = true
    name = process.argv[3]
} else if (!name) {
    console.log(
        'Usage: node run.js [--quiet] [TASK]\n' +
        'Examples: "node run.js Week1Task3", "node run.js 1-3", "node run.js --quiet 1-3"'
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

const t0 = process.hrtime()

task.on('exit', (code) => {
    if (code === 0) {
        const t1 = process.hrtime(t0)
        if (quietMode) {
            return
        }

        const outputBuffer = readFileSync(join(__dirname, 'resources/' + name + '/output.txt'))
        console.log(outputBuffer.toString())
        console.log('\nProcess execution time: ' + t1[0] + '.' + Math.floor(t1[1].toString().substring(0, 5)) + ' s')
    }
})
