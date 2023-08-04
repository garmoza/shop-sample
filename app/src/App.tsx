import React, { useEffect, useState } from 'react'
import type { FC } from 'react'
import './App.css'
import axios from 'axios'

const BACKEND_URL = 'http://localhost:8080/users'

interface User {
  id: string
  username: string
  email: string
  balance: number
  status: 'FROZEN' | 'ENABLE' | 'DISABLED'
  authorities: string[]
}

const App: FC = () => {
  const [url] = useState(BACKEND_URL)
  const [users, setUsers] = useState<User[]>([])

  useEffect(() => {
    const fetchData = async (): Promise<void> => {
      const result = await axios(BACKEND_URL)

      setUsers(result.data)
    }

    void fetchData()
  }, [url])

  return (
    <>
      <UsersList users={users} />
    </>
  )
}

interface UsersListProps {
  users: User[]
}

const UsersList: FC<UsersListProps> = ({ users }) => {
  const items = users.map((user) => (
    <div
      key={user.id}
    >
      <p>
      <b> id: </b>{user.id}
      <b> username: </b>{user.username}
      <b> email: </b>{user.email}
      <b> balance: </b>{user.balance}
      <b> status: </b>{user.status}
      <b> authorities: </b> {user.authorities.map(authority => authority + ' ')}
      </p>
      <button
        onClick={() => {
          axios.delete(BACKEND_URL + '/' + user.id)
            .then(function (response) {
              console.log(response)
            })
            .catch(function (error) {
              console.log(error)
            })
        }}>
        Delete
      </button>
    </div>
  ))

  return (
    <>
      {items}
    </>
  )
}

export default App
