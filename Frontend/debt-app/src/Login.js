import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Login() {
    const [email, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);


    const navigate = useNavigate();

    const validateForm = () => {
        if(!email || !password){
            setError('All fields are required');
            return false;
        }
        setError('');
        return true;
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        if(!validateForm()){
            return;
        }
        setLoading(true);

        const formDetails = {
            email: email,
            password: password
        };

        try{
            const response = await fetch('http://localhost:8080/v1/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formDetails),
            });

            setLoading(false);

            if(response.ok){
                const data = await response.json();
                localStorage.setItem('token', data.access_token);
                localStorage.setItem('email', email);
                navigate('/dashboard');
            } else{
                const errorData = await response.json();
                setError(errorData.detail || 'Authentication failed!');
            }
        } catch (error){
            setLoading(false);
            setError('An error occured. Try again later.');
        }
    };

    return (
        <div className="container">
            <h2 className='center-text paddings'>Welcome in 
                <span className="color-change"> DebtApp</span>
            </h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Email:</label>
                    <input
                        type="text"
                        value={email}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <button type="submit" disabled={loading}>
                    {loading ? 'Logging in...' : 'Login'}
                </button>
                {error && <p style={{ color: 'red' }}>{typeof error === 'string' ? error : JSON.stringify(error)}</p>}
            </form>
            <footer className="center-button">
                <h4 style={{width:'100%'}} className='center-text'>Sign up!</h4>
                <button className='outline' onClick={() => navigate('/')}>Register</button>
            </footer>
        </div>
    );

}
export default Login;