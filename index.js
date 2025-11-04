import Link from 'next/link';
export default function Home(){
  return (
    <div>
      <h1>Ticketing System — Frontend</h1>
      <p><Link href="/login">Login</Link> | <Link href="/dashboard">Dashboard</Link></p>
      <p>API base assumed at <code>http://localhost:8080/api</code></p>
    </div>
  )
}
