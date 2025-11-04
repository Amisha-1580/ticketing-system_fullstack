import {useState} from 'react';
import {useRouter} from 'next/router';

export default function Login(){
  const [email,setEmail]=useState('');
  const [password,setPassword]=useState('');
  const router = useRouter();

  async function submit(e){
    e.preventDefault();
    const res = await fetch('http://localhost:8080/api/auth/login',{
      method:'POST',
      headers:{'Content-Type':'application/json'},
      body: JSON.stringify({email,password})
    });
    if(res.ok){
      const data = await res.json();
      // naive: store user object in localStorage
      localStorage.setItem('user', JSON.stringify(data.user)); localStorage.setItem('token', data.token);
      router.push('/dashboard');
    } else {
      alert('login failed');
    }
  }

  return (
    <div>
      <h2>Login</h2>
      <form onSubmit={submit}>
        <div><input placeholder="email" value={email} onChange={e=>setEmail(e.target.value)} /></div>
        <div><input placeholder="password" type="password" value={password} onChange={e=>setPassword(e.target.value)} /></div>
        <button type="submit">Login</button>
      </form>
    </div>
  )
}
