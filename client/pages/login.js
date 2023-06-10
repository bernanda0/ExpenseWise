import { useState } from 'react';
import { useRouter } from 'next/router';
import axios from 'axios';

const baseURL = "https://api-expensewise.netlify.app/.netlify/functions/server/";

export default function Login() {
    const router = useRouter();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleChangeEmail = event => {
        setEmail(event.target.value);
    };

    const handleChangePassword = event => {
        setPassword(event.target.value);
    };

    const handleSubmit = event => {
        event.preventDefault();

        const user = {
            email: email,
            password: password
        };

        console.log(user);
        axios.post(`${baseURL}/auth/login/local`,  user )
            .then(res => {
                console.log(res);
                
                console.log(res.data);
                router.push("/dashboard");
            })
            .catch(error => {
                console.log("halo");
                console.error(error.message);
            });
    };

    function loginGoogle() {
        axios.get(`${baseURL}/auth/google`).then((response) => {
            setPost(response.data);
        });
    }

    function loginLocal() {
        axios.post(`${baseURL}/auth/login/local`).then((response) => {
            setPost(response.data);
        });
    }

    return (
        <div className='grid grid-cols-2 gap-4'>
            <div className='shadow-lg'> <img src="/LogoExpenseHitam.png" width={500} height={500} />  </div>
            <div className='shadow-lg min-h-full flex item-center justify-center'>
                <div className='max-w-md w-full space-y-8'>
                    <div>
                        <h2 className='text-center text-3xl font-extrabold text-white'>Sign In to Your Account</h2>
                        <p className='mt-2 text-center text-sm text-gray-600'>
                            or
                            <a href='/signUp' className='font-medium tpext-yellow-400 hover:text-yellow-500 px-2'>Sign Up</a>
                        </p>
                    </div>

                    <form onSubmit={handleSubmit} className='mt-8 space-y-6' action='#' method='POST'>
                        {/* email */}
                        <div>
                            <input
                                type='email'
                                autoComplete='off'
                                required
                                className='appearance-none rounded relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-full focus:outline-none focus:ring-yellow-500 focus:border-yellow-500 focus:z-10 sm:text-sm'
                                placeholder='Email address'
                                value={email}
                                onChange={handleChangeEmail}
                            />
                        </div>
                        {/* password*/}
                        <div>
                            <input
                                type='password'
                                autoComplete='off'
                                required
                                className='appearance-none rounded relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-full focus:outline-none focus:ring-yellow-500 focus:border-yellow-500 focus:z-10 sm:text-sm'
                                placeholder='Password'
                                value={password}
                                onChange={handleChangePassword}
                            />
                        </div>
                        {/* Garis pembatas*/}
                        <div className="flex items-center justify-center">
                            <div className="border-t border-gray-300 w-1/4"></div>
                            <p className="px-4 text-gray-500">or</p>
                            <div className="border-t border-gray-300 w-1/4"></div>
                        </div>

                        {/* Google Sign In */}
                        <div>
                            <button onClick={loginGoogle} className='group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-yellow-400 hover:bg-yellow-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-yellow-500'>Sign In with Google</button>
                        </div>

                        <div className='text-sm'>
                            <a href='#' className='font-medium text-yellow-400 hover:text-yellow-500 px-2'>Forgot your password?</a>
                        </div>

                        <div>
                            <button className='group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-yellow-400 hover:bg-yellow-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-yellow-500'>Sign In</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    )
}
